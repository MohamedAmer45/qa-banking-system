#!/usr/bin/env bash
#
# Run a JMeter shape and gate on the result.
#
# JMeter's non-GUI mode exits 0 whether or not the numbers were acceptable, so
# it reports rather than gates. This wraps it with analyze.mjs, which enforces
# the correctness invariants and fails the build on them, and generates JMeter's
# own HTML dashboard alongside the raw results.
#
# Shapes:
#   load         steady traffic at a fixed rate         gated
#   stress       ramp well past expected load           observed
#   spike        steady traffic, then a sudden burst    observed
#   endurance    modest load held for a long time       gated
#   concurrency  simultaneous debits on one account     gated
#
# "Observed" means 5xx does not fail the run. A stress ramp that returned no
# errors has not found the limit it went looking for, so failing on that would
# punish the test for working. Correctness is still gated in every shape: the
# ledger must reconcile whatever else happens.
#
# Usage:
#   ./run.sh load        [threads] [duration]
#   ./run.sh stress      [threads] [duration]
#   ./run.sh spike       [baseline] [spikeThreads]
#   ./run.sh endurance   [threads] [duration]
#   ./run.sh concurrency [threads] [amountMajor]
#
# Requires JMETER_HOME, or jmeter on PATH.
set -euo pipefail

SHAPE="${1:-load}"
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
JTL="results/${SHAPE}.jtl"
DASHBOARD="results/dashboard-${SHAPE}"

# JMeter refuses to write a dashboard into a directory that already has one,
# and refuses to append to an existing .jtl.
rm -rf "$JTL" "$DASHBOARD"
rm -f results/balance-before.txt results/balance-after.txt results/beneficiary-bank.txt

# -e -o generates the HTML dashboard the reporting stack advertises: the
# response-time-over-time and throughput graphs a percentile table cannot show.
run_jmeter() {
  # JMeter failing to run is not the same as a test failing, and the two used to
  # look identical: a non-zero exit killed the step under `set -e` before the
  # analyzer ran, so the build failed with nothing said about why. At high thread
  # counts that is usually the load generator running out of memory or threads
  # rather than anything wrong with the application, and it needs saying.
  set +e
  "$JMETER" -n -t "$1" -l "$JTL" -e -o "$DASHBOARD" \
    -Jhost="$HOST" -Jport="$PORT" -Jprotocol="$PROTOCOL" \
    "${@:2}"
  local code=$?
  set -e

  if [ "$code" -ne 0 ]; then
    echo "JMeter itself exited ${code} before the run completed."
    echo "--- tail of jmeter.log ---"
    tail -40 jmeter.log 2>/dev/null || echo "(no jmeter.log)"

    if [ "${GITHUB_ACTIONS:-}" = "true" ]; then
      detail=$(tail -5 jmeter.log 2>/dev/null | tr '\n' " " | tr -d '\r')
      echo "::error title=JMeter did not run::exit ${code}. ${detail}"
    fi
    return "$code"
  fi
}

case "$SHAPE" in
  load)
    THREADS="${2:-10}"; DURATION="${3:-60}"
    echo "Load: ${THREADS} paced threads for ${DURATION}s against ${PROTOCOL}://${HOST}:${PORT}"
    run_jmeter plans/read-path-load.jmx \
      -Jthreads="$THREADS" -Jrampup=10 -Jduration="$DURATION"
    node analyze.mjs --jtl "$JTL" --plan load --mode gate --results-dir results
    ;;

  stress)
    # Far more threads than the load shape, ramped over most of the run, so the
    # offered rate climbs steadily and the point where it degrades is visible in
    # the per-quarter latency the analyzer prints.
    #
    # Raised from 150 to 400. At 150 the application sustained 79,845 samples
    # with zero errors and a flat 3-4 ms p95 across every quarter, which means
    # the ramp never reached the knee it exists to find. A stress shape that
    # finds no limit has measured nothing.
    #
    # If 400 also comes back flat, the honest conclusion is that the bottleneck
    # is the runner or JMeter itself rather than the application, and the next
    # step is a bigger load generator rather than a bigger number here.
    THREADS="${2:-400}"; DURATION="${3:-180}"
    echo "Stress: ramping to ${THREADS} threads over ${DURATION}s"
    run_jmeter plans/read-path-load.jmx \
      -Jthreads="$THREADS" -Jrampup=$(( DURATION * 2 / 3 )) -Jduration="$DURATION" \
      -JthinkTimeMs=100 -JthinkJitterMs=100
    node analyze.mjs --jtl "$JTL" --plan stress --mode observe --results-dir results
    ;;

  spike)
    # A steady baseline, then a burst of simultaneous new sign-ins partway
    # through. What matters is whether the baseline recovers afterwards, which
    # the per-quarter latency shows.
    BASELINE="${2:-10}"; SPIKE="${3:-100}"
    echo "Spike: ${BASELINE} steady threads, then ${SPIKE} arriving at once"
    run_jmeter plans/read-path-load.jmx \
      -Jthreads="$BASELINE" -Jrampup=5 -Jduration="${SPIKE_TOTAL:-120}" \
      -JspikeThreads="$SPIKE" -JspikeDelay="${SPIKE_AT:-40}" -JspikeDuration="${SPIKE_FOR:-30}" -JspikeRampup=0
    node analyze.mjs --jtl "$JTL" --plan spike --mode observe --results-dir results
    ;;

  endurance)
    # Modest load held long enough for anything that accumulates to show up as
    # latency drift between the first and last quarter.
    THREADS="${2:-10}"; DURATION="${3:-900}"
    echo "Endurance: ${THREADS} threads for ${DURATION}s"
    run_jmeter plans/read-path-load.jmx \
      -Jthreads="$THREADS" -Jrampup=10 -Jduration="$DURATION"
    node analyze.mjs --jtl "$JTL" --plan endurance --mode gate --results-dir results
    ;;

  concurrency)
    THREADS="${2:-20}"; AMOUNT="${3:-50}"
    echo "Concurrency: ${THREADS} threads, ${AMOUNT} EGP each"
    run_jmeter plans/transfer-concurrency.jmx \
      -Jthreads="$THREADS" -JamountMajor="$AMOUNT" -JresultsDir=results
    node analyze.mjs --jtl "$JTL" --plan concurrency --mode gate \
      --amount-major "$AMOUNT" --results-dir results
    ;;

  *)
    echo "Unknown shape '$SHAPE'." >&2
    echo "Use load, stress, spike, endurance or concurrency." >&2
    exit 1
    ;;
esac

echo "HTML dashboard: ${DASHBOARD}/index.html"
