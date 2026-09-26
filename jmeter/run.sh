#!/usr/bin/env bash
#
# Run a JMeter plan and gate on the result.
#
# JMeter's non-GUI mode exits 0 whether or not the numbers were acceptable, so
# it reports rather than gates. This wraps it with analyze.mjs, which enforces
# the correctness invariants and fails the build on them.
#
# Usage:
#   ./run.sh concurrency [threads] [amountMajor]
#   ./run.sh read        [threads] [duration]
#
# Requires JMETER_HOME, or jmeter on PATH.
set -euo pipefail

PLAN="${1:-concurrency}"
HOST="${JMETER_HOST:-127.0.0.1}"
PORT="${JMETER_PORT:-3000}"
PROTOCOL="${JMETER_PROTOCOL:-http}"

here="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$here"

if [ -n "${JMETER_HOME:-}" ]; then
  JMETER="$JMETER_HOME/bin/jmeter"
elif command -v jmeter >/dev/null 2>&1; then
  JMETER="jmeter"
else
  echo "JMeter not found. Set JMETER_HOME or put jmeter on PATH." >&2
  echo "Download: https://jmeter.apache.org/download_jmeter.cgi" >&2
  exit 1
fi

mkdir -p results
rm -f results/balance-before.txt results/balance-after.txt

case "$PLAN" in
  concurrency)
    THREADS="${2:-20}"
    AMOUNT="${3:-50}"
    JTL="results/concurrency.jtl"
    rm -f "$JTL"

    echo "Transfer concurrency: ${THREADS} threads, ${AMOUNT} EGP each, against ${PROTOCOL}://${HOST}:${PORT}"

    "$JMETER" -n -t plans/transfer-concurrency.jmx -l "$JTL" \
      -Jhost="$HOST" -Jport="$PORT" -Jprotocol="$PROTOCOL" \
      -Jthreads="$THREADS" -JamountMajor="$AMOUNT" -JresultsDir=results

    node analyze.mjs --jtl "$JTL" --plan concurrency \
      --amount-major "$AMOUNT" --results-dir results
    ;;

  read)
    THREADS="${2:-10}"
    DURATION="${3:-60}"
    JTL="results/read.jtl"
    rm -f "$JTL"

    echo "Read-path load: ${THREADS} threads for ${DURATION}s against ${PROTOCOL}://${HOST}:${PORT}"

    "$JMETER" -n -t plans/read-path-load.jmx -l "$JTL" \
      -Jhost="$HOST" -Jport="$PORT" -Jprotocol="$PROTOCOL" \
      -Jthreads="$THREADS" -Jrampup=$(( THREADS < 10 ? THREADS : 10 )) \
      -Jduration="$DURATION"

    node analyze.mjs --jtl "$JTL" --plan read --results-dir results
    ;;

  *)
    echo "Unknown plan '$PLAN'. Use 'concurrency' or 'read'." >&2
    exit 1
    ;;
esac
