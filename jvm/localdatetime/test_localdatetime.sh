#!/usr/bin/env bash

set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
build_dir="$(mktemp -d)"
trap 'rm -rf "$build_dir"' EXIT

javac -d "$build_dir" "$script_dir/DateTimeDemo.java"
output="$(java -cp "$build_dir" localdatetime.DateTimeDemo)"

grep -Eq '^Current date and time: [0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}' <<<"$output"
grep -Eq '^Formatted date and time: [0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$' <<<"$output"

printf '%s\n' 'DateTime smoke test passed.'
