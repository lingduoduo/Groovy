#!/usr/bin/env bash

set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
build_dir="$(mktemp -d)"
trap 'rm -rf "$build_dir"' EXIT

javac -d "$build_dir" "$script_dir/LambdaDemo.java"
output="$(java -cp "$build_dir" lambda.LambdaDemo)"

grep -Fq 'Consumed: Java Lambda' <<<"$output"
grep -Eq '^Generated random number: [0-9]+$' <<<"$output"
grep -Fq 'String length: 12' <<<"$output"
grep -Fq 'Is 15 greater than 10? true' <<<"$output"

printf '%s\n' 'Lambda smoke test passed.'
