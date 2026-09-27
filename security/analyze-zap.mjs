#!/usr/bin/env node
/*
 * Turns a ZAP baseline report into a pass or a fail.
 *
 * ZAP and its GitHub action can gate on their own, via a rules file passed with
 * -c. That was tried first and silently did nothing: the action copies the rules
 * file into the container by basename but hands ZAP the full relative path, so
 * ZAP looked for a file that was not there, found no config, and applied none of
 * the suppressions. Nothing in the output said so — every rule simply stayed at
 * its default and the build failed on alerts that had already been reviewed.
 *
 * So the rules file is applied here instead. Same file, same format, read
 * directly, and the decision is visible in the output rather than inferred from
 * an exit code. It can also be run against a saved report on a laptop, which the
 * action's internal gating cannot.
 *
 * Usage:
 *   node analyze-zap.mjs --report report_json.json --rules security/zap-rules.tsv
 */

import { readFileSync, existsSync } from "node:fs";

function arg(name, fallback = undefined) {
  const i = process.argv.indexOf(`--${name}`);
  if (i !== -1 && process.argv[i + 1]) return process.argv[i + 1];
  if (fallback !== undefined) return fallback;
  throw new Error(`Missing required argument --${name}`);
}

/**
 * Parse the ZAP rules file.
 *
 * Format is `<rule id>\t<WARN|IGNORE|FAIL>\t<reason>`, with `#` comments. A rule
 * with no reason is rejected rather than honoured: an unexplained suppression is
 * how a scanner quietly stops reporting something that matters, and this file is
 * the one place that decision is recorded.
 */
function readRules(path) {
  const rules = new Map();
  const malformed = [];

  for (const [index, raw] of readFileSync(path, "utf8").split(/\r?\n/).entries()) {
    const line = raw.trim();
    if (!line || line.startsWith("#")) continue;

    const parts = raw.split("\t");
    const [id, action, ...rest] = parts.map(p => p.trim());
    const reason = rest.join(" ").trim();

    if (!/^\d+$/.test(id ?? "") || !["WARN", "IGNORE", "FAIL"].includes(action ?? "")) {
      malformed.push(`line ${index + 1}: ${line}`);
      continue;
    }

    if (action === "IGNORE" && reason.length === 0) {
      malformed.push(`line ${index + 1}: rule ${id} is IGNOREd with no reason given`);
      continue;
    }

    rules.set(id, { action, reason });
  }

  return { rules, malformed };
}

const RISK_ORDER = { High: 0, Medium: 1, Low: 2, Informational: 3 };

function riskOf(alert) {
  return (alert.riskdesc ?? "Informational").split(" ")[0];
}

const reportPath = arg("report");
const rulesPath = arg("rules");

if (!existsSync(reportPath)) {
  console.error(`No ZAP report at ${reportPath} — the scan did not produce one.`);
  process.exit(1);
}

const { rules, malformed } = readRules(rulesPath);

if (malformed.length > 0) {
  console.error("The rules file has entries this cannot act on:");
  malformed.forEach(m => console.error(`  - ${m}`));
  process.exit(1);
}

const report = JSON.parse(readFileSync(reportPath, "utf8"));
const alerts = (report.site ?? []).flatMap(site => site.alerts ?? []);

const failing = [];
const suppressed = [];

for (const alert of alerts) {
  const id = String(alert.pluginid);
  const rule = rules.get(id);
  const row = {
    id,
    risk: riskOf(alert),
    name: alert.name,
    count: (alert.instances ?? []).length,
    example: (alert.instances ?? [])[0]?.uri ?? "",
    reason: rule?.reason ?? ""
  };

  if (rule?.action === "IGNORE") suppressed.push(row);
  else failing.push(row);
}

const bySeverity = (a, b) =>
  (RISK_ORDER[a.risk] ?? 9) - (RISK_ORDER[b.risk] ?? 9) || a.id.localeCompare(b.id);

failing.sort(bySeverity);
suppressed.sort(bySeverity);

console.log("");
console.log("ZAP baseline analysis");
console.log("=".repeat(72));
console.log(`alerts reported      ${alerts.length}`);
console.log(`reviewed & accepted  ${suppressed.length}`);
console.log(`unreviewed           ${failing.length}`);

if (suppressed.length > 0) {
  console.log("");
  console.log("accepted, with the reason recorded in the rules file:");
  for (const r of suppressed) {
    console.log(`  [${r.risk}] ${r.id} ${r.name} x${r.count}`);
    console.log(`        ${r.reason}`);
  }
}

if (failing.length > 0) {
  console.log("");
  console.log("NOT accepted:");
  for (const r of failing) {
    console.log(`  [${r.risk}] ${r.id} ${r.name} x${r.count}  e.g. ${r.example}`);
  }

  if (process.env.GITHUB_ACTIONS === "true") {
    for (const r of failing) {
      console.error(
        `::error title=ZAP ${r.id} (${r.risk})::${r.name} — ${r.count} instance(s), ` +
        `e.g. ${r.example}. Fix it, or record a reason in security/zap-rules.tsv.`
      );
    }
  }

  console.error("");
  console.error(
    `FAILED — ${failing.length} alert(s) have neither been fixed nor given a ` +
    `reason in security/zap-rules.tsv.`
  );
  process.exit(1);
}

console.log("");
console.log("PASSED — every alert is either absent or accepted with a reason.");
