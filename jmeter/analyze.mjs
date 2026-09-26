#!/usr/bin/env node
/*
 * Turns a JMeter result file into a pass or a fail.
 *
 * JMeter's non-GUI run exits 0 whether or not the numbers were acceptable, so
 * on its own it reports rather than gates. This reads the .jtl and enforces the
 * properties that actually matter, then prints what it measured.
 *
 * What is enforced, and why only these:
 *
 *   Correctness invariants are absolute. A concurrent debit must answer 201 or
 *   409 and nothing else, no sample may return 5xx, and the balance must fall
 *   by exactly the amount plus fee of every success. Those hold on any machine,
 *   so failing on them is never environmental.
 *
 *   Latency is reported, not enforced. See thresholds.json for the reason.
 *
 * Usage:
 *   node analyze.mjs --jtl results/concurrency.jtl --plan concurrency \
 *                    --amount-major 50 --results-dir results
 */

import { readFileSync, existsSync } from "node:fs";
import { join } from "node:path";

function arg(name, fallback = undefined) {
  const i = process.argv.indexOf(`--${name}`);
  if (i !== -1 && process.argv[i + 1]) return process.argv[i + 1];
  if (fallback !== undefined) return fallback;
  throw new Error(`Missing required argument --${name}`);
}

/* JMeter writes a CSV header, and no field this plan produces contains a comma
 * except failureMessage, which is read last and only for display. */
function readJtl(path) {
  const text = readFileSync(path, "utf8").trim();
  const [header, ...lines] = text.split(/\r?\n/);
  const columns = header.split(",");

  return lines.filter(Boolean).map(line => {
    const cells = line.split(",");
    const row = {};
    columns.forEach((c, i) => { row[c] = cells[i]; });
    // Anything after the known columns belongs to the failure message.
    if (cells.length > columns.length) {
      row.failureMessage = cells.slice(columns.indexOf("failureMessage")).join(",");
    }
    return row;
  });
}

function mean(values) {
  if (values.length === 0) return 0;
  return Math.round(values.reduce((a, b) => a + b, 0) / values.length);
}

/** Samples per second over the wall-clock span the run actually covered. */
function throughput(rows) {
  if (rows.length < 2) return 0;
  const stamps = rows.map(r => Number(r.timeStamp)).filter(Number.isFinite);
  const elapsedSeconds =
    (Math.max(...stamps) - Math.min(...stamps)) / 1000;
  return elapsedSeconds > 0
    ? Math.round((rows.length / elapsedSeconds) * 10) / 10
    : 0;
}

function percentile(values, p) {
  if (values.length === 0) return 0;
  const sorted = [...values].sort((a, b) => a - b);
  const index = Math.ceil((p / 100) * sorted.length) - 1;
  return sorted[Math.max(0, index)];
}

/**
 * The per-transfer fee, mirrored from src/banking.js.
 *
 * Only an EXTERNAL transfer is charged, and a transfer is EXTERNAL exactly when
 * the beneficiary is not at NOVABANK. The bank therefore cannot be assumed: the
 * plan records which beneficiary it chose, and this reads it. Assuming external
 * is what made the first CI run fail — the seeded beneficiary it picked was
 * internal, so the balance moved by the amount alone and the reconciliation was
 * short by exactly the fees.
 */
function feeMinor(amountMinor, bankName) {
  const external = bankName !== null && bankName !== "NOVABANK";
  return external ? Math.max(500, Math.round(amountMinor * 0.001)) : 0;
}

const jtlPath = arg("jtl");
const plan = arg("plan");

/*
 * "gate" fails the build on a correctness breach. "observe" reports the same
 * numbers but never fails on 5xx, because some shapes exist precisely to find
 * where the application degrades: a stress ramp that returned no errors has not
 * found the limit it was looking for. Infrastructure problems (an empty result
 * file, a plan that could not set itself up) still fail in either mode.
 */
const mode = arg("mode", "gate");
if (mode !== "gate" && mode !== "observe") {
  throw new Error(`--mode must be gate or observe, got ${mode}`);
}
const resultsDir = arg("results-dir", "results");
const amountMajor = Number(arg("amount-major", "50"));

const rows = readJtl(jtlPath);
const failures = [];
const notes = [];

if (rows.length === 0) {
  console.error(`No samples in ${jtlPath} — the plan did not run.`);
  if (process.env.GITHUB_ACTIONS === "true") {
    console.error(`::error title=JMeter ${plan}::no samples were recorded`);
  }
  process.exit(1);
}

/* ---------------------------------------------------------------- universal */

const serverErrors = rows.filter(r => /^5\d\d$/.test(r.responseCode));
if (serverErrors.length > 0 && mode === "observe") {
  notes.push(
    `degraded             ${serverErrors.length} of ${rows.length} samples returned 5xx`
  );
}
if (serverErrors.length > 0 && mode === "gate") {
  const codes = [...new Set(serverErrors.map(r => `${r.label} -> ${r.responseCode}`))];
  failures.push(`${serverErrors.length} sample(s) returned 5xx: ${codes.join(", ")}`);
}

const assertionFailures = rows.filter(r => r.success === "false");
if (assertionFailures.length > 0 && mode === "gate") {
  const detail = [...new Set(assertionFailures.map(
    r => `${r.label} (${r.responseCode}) ${r.failureMessage ?? ""}`.trim()
  ))].slice(0, 5);
  failures.push(
    `${assertionFailures.length} sample(s) failed an assertion: ${detail.join(" | ")}`
  );
}

/* ------------------------------------------------------- per-plan invariants */

if (plan === "concurrency") {
  const transfers = rows.filter(r => r.label === "POST transfer");

  if (transfers.length === 0) {
    failures.push("No transfer samples — setUp probably could not choose an account");
  }

  const unexpected = transfers.filter(
    r => r.responseCode !== "201" && r.responseCode !== "409"
  );
  if (unexpected.length > 0) {
    const codes = [...new Set(unexpected.map(r => r.responseCode))];
    failures.push(
      `A concurrent debit must answer 201 or 409; saw ${codes.join(", ")}`
    );
  }

  const created = transfers.filter(r => r.responseCode === "201").length;
  const refused = transfers.filter(r => r.responseCode === "409").length;

  const beforePath = join(resultsDir, "balance-before.txt");
  const afterPath = join(resultsDir, "balance-after.txt");

  if (!existsSync(beforePath) || !existsSync(afterPath)) {
    failures.push(
      "The plan did not record both balances, so the ledger could not be reconciled"
    );
  } else {
    const before = Number(readFileSync(beforePath, "utf8").trim());
    const after = Number(readFileSync(afterPath, "utf8").trim());
    const moved = before - after;

    const amountMinor = Math.round(amountMajor * 100);

    const bankPath = join(resultsDir, "beneficiary-bank.txt");
    const bankName = existsSync(bankPath)
      ? readFileSync(bankPath, "utf8").trim()
      : null;

    if (bankName === null) {
      failures.push(
        "The plan did not record which beneficiary it paid, so the fee " +
        "(and therefore the expected debit) is unknown"
      );
    }

    const fee = feeMinor(amountMinor, bankName);
    const expected = created * (amountMinor + fee);

    /*
     * The strongest statement this test can make. The fee is deterministic, so
     * the debit is known exactly: any other figure means the application either
     * lost money or invented it under contention.
     */
    if (moved !== expected) {
      failures.push(
        `Ledger did not reconcile: balance fell by ${moved} minor units, but ` +
        `${created} successful transfer(s) of ${amountMinor} plus a ` +
        `${fee} fee (beneficiary at ${bankName}) should move exactly ${expected}`
      );
    }

    if (created === 0 && moved !== 0) {
      failures.push(`No transfer succeeded, yet the balance moved by ${moved}`);
    }

    notes.push(`opening balance      ${before.toLocaleString()} minor units`);
    notes.push(`closing balance      ${after.toLocaleString()} minor units`);
    notes.push(`moved                ${moved.toLocaleString()} minor units`);
    notes.push(`beneficiary bank     ${bankName} (fee ${fee})`);
    notes.push(`reconciles to        ${created} x (${amountMinor} + ${fee})`);
  }

  notes.push(`created (201)        ${created}`);
  notes.push(`refused (409)        ${refused}`);

  if (refused === 0) {
    notes.push(
      "note                 every debit succeeded, so the refusal path was not " +
      "exercised; raise --amount-major or the thread count to force contention " +
      "against the balance"
    );
  }
}

/* ------------------------------------------------------- reported, not gated */

const elapsed = rows.map(r => Number(r.elapsed)).filter(Number.isFinite);
const errorRate = (assertionFailures.length / rows.length) * 100;
const byLabel = new Map();
for (const r of rows) {
  if (!byLabel.has(r.label)) byLabel.set(r.label, []);
  byLabel.get(r.label).push(Number(r.elapsed));
}

console.log(`
JMeter analysis - ${plan} (${mode})`);
console.log("=".repeat(72));
console.log(`samples              ${rows.length}`);
console.log(`errors               ${assertionFailures.length} (${errorRate.toFixed(2)}%)`);
console.log(`throughput           ${throughput(rows)} req/s`);
console.log(`avg                  ${mean(elapsed)} ms`);
console.log(`p50 / p90            ${percentile(elapsed, 50)} / ${percentile(elapsed, 90)} ms`);
console.log(`p95 / p99 / max      ${percentile(elapsed, 95)} / ${percentile(elapsed, 99)} / ${Math.max(...elapsed)} ms`);
notes.forEach(n => console.log(n));

console.log("");
console.log("per request");
console.log(
  `  ${"label".padEnd(28)} ${"n".padEnd(6)} ${"avg".padEnd(7)} ${"p90".padEnd(7)} ${"p95".padEnd(7)} p99`
);
for (const [label, values] of [...byLabel].sort()) {
  console.log(
    `  ${label.padEnd(28)} ${String(values.length).padEnd(6)} ` +
    `${String(mean(values)).padEnd(7)} ${String(percentile(values, 90)).padEnd(7)} ` +
    `${String(percentile(values, 95)).padEnd(7)} ${percentile(values, 99)}`
  );
}

/*
 * Latency over time, in quarters. A soak that ends slower than it started is
 * the signature of something accumulating, and an aggregate p95 hides that
 * completely by averaging a healthy beginning with a degraded end.
 */
if (rows.length >= 40) {
  const ordered = [...rows].sort((a, b) => Number(a.timeStamp) - Number(b.timeStamp));
  const size = Math.floor(ordered.length / 4);
  const quarters = [0, 1, 2, 3].map(q =>
    ordered.slice(q * size, q === 3 ? ordered.length : (q + 1) * size)
           .map(r => Number(r.elapsed))
  );

  console.log("");
  console.log("latency over the run (quarters)");
  quarters.forEach((values, q) => {
    console.log(`  Q${q + 1}  avg=${String(mean(values)).padEnd(7)} p95=${percentile(values, 95)} ms`);
  });

  const drift = percentile(quarters[3], 95) - percentile(quarters[0], 95);
  console.log(`  drift in p95, first quarter to last: ${drift >= 0 ? "+" : ""}${drift} ms`);
}

console.log(
  "\nLatency above is reported, not enforced — see thresholds.json for why."
);

if (failures.length > 0) {
  console.error("\nFAILED");
  failures.forEach(f => console.error(`  - ${f}`));

  /*
   * Also emit workflow annotations, so a failure is legible on the run
   * summary and in a pull request without opening the log.
   */
  if (process.env.GITHUB_ACTIONS === "true") {
    failures.forEach(f => {
      console.error(`::error title=JMeter ${plan}::${f}`);
    });
  }

  process.exit(1);
}

/*
 * Publish the headline numbers to the run summary too. Without this a green
 * performance run reports only that it was green, and the figure that matters
 * (how many debits committed against how many were refused) stays buried in a
 * log nobody opens.
 */
if (process.env.GITHUB_ACTIONS === "true") {
  const summary = [`${rows.length} samples`, ...notes.map(n => n.replace(/ +/g, " ").trim())]
    .join("; ");
  console.log(`::notice title=JMeter ${plan}::${summary}`);
}

console.log("\nPASSED — every enforced invariant held.");
