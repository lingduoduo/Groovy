#!/usr/bin/env bash

set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
build_dir="$(mktemp -d)"
trap 'rm -rf "$build_dir"' EXIT

javac -d "$build_dir" "$script_dir/StreamDemo.java"
output="$(java -cp "$build_dir" streamapi.StreamDemo)"

grep -Fqx 'Filtered, distinct, and sorted: [6, 4, 2]' <<<"$output"
grep -Fqx 'Mapped sum: 68' <<<"$output"
grep -Fqx 'Grouped result: {false=[1, 2, 3], true=[4, 5, 6, 6, 7]}' <<<"$output"

printf '%s\n' 'Stream API smoke test passed.'
