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

function percentile(values, p) {
  if (values.length === 0) return 0;
  const sorted = [...values].sort((a, b) => a - b);
  const index = Math.ceil((p / 100) * sorted.length) - 1;
  return sorted[Math.max(0, index)];
}

/** The application's external-transfer fee, mirrored from src/banking.js. */
function feeMinor(amountMinor) {
  return Math.max(500, Math.round(amountMinor * 0.001));
}

const jtlPath = arg("jtl");
const plan = arg("plan");
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
if (serverErrors.length > 0) {
  const codes = [...new Set(serverErrors.map(r => `${r.label} -> ${r.responseCode}`))];
  failures.push(`${serverErrors.length} sample(s) returned 5xx: ${codes.join(", ")}`);
}

const assertionFailures = rows.filter(r => r.success === "false");
if (assertionFailures.length > 0) {
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
    const expected = created * (amountMinor + feeMinor(amountMinor));

    /*
     * The strongest statement this test can make. The fee is deterministic, so
     * the debit is known exactly: any other figure means the application either
     * lost money or invented it under contention.
     */
    if (moved !== expected) {
      failures.push(
        `Ledger did not reconcile: balance fell by ${moved} minor units, but ` +
        `${created} successful transfer(s) of ${amountMinor} plus a ` +
        `${feeMinor(amountMinor)} fee should move exactly ${expected}`
      );
    }

    if (created === 0 && moved !== 0) {
      failures.push(`No transfer succeeded, yet the balance moved by ${moved}`);
    }

    notes.push(`opening balance      ${before.toLocaleString()} minor units`);
    notes.push(`closing balance      ${after.toLocaleString()} minor units`);
    notes.push(`moved                ${moved.toLocaleString()} minor units`);
    notes.push(`reconciles to        ${created} x (${amountMinor} + ${feeMinor(amountMinor)})`);
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
const byLabel = new Map();
for (const r of rows) {
  if (!byLabel.has(r.label)) byLabel.set(r.label, []);
  byLabel.get(r.label).push(Number(r.elapsed));
}

console.log(`\nJMeter analysis — ${plan}`);
console.log("=".repeat(64));
console.log(`samples              ${rows.length}`);
console.log(`errors               ${assertionFailures.length}`);
console.log(`p50 / p95 / max      ${percentile(elapsed, 50)} / ${percentile(elapsed, 95)} / ${Math.max(...elapsed)} ms`);
notes.forEach(n => console.log(n));

console.log("\nper request");
for (const [label, values] of [...byLabel].sort()) {
  console.log(
    `  ${label.padEnd(26)} n=${String(values.length).padEnd(5)} ` +
    `p50=${String(percentile(values, 50)).padEnd(7)} p95=${percentile(values, 95)} ms`
  );
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

console.log("\nPASSED — every enforced invariant held.");
