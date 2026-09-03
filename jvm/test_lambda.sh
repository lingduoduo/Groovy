#!/usr/bin/env bash

set -euo pipefail

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
build_dir="$(mktemp -d)"
trap 'rm -rf "$build_dir"' EXIT

javac -d "$build_dir" "$script_dir/Lambda.java"
output="$(java -cp "$build_dir" Lambda)"

grep -Fq "Hello, I'm Ling" <<<"$output"
grep -Fq 'Sorted colors: [white, red, blue, black]' <<<"$output"
grep -Fq 'Affordable orders: [Milkshake, Burger]' <<<"$output"
grep -Fq 'Odd numbers: [1, 3]' <<<"$output"
grep -Fq 'Sum: 20' <<<"$output"

printf '%s\n' 'Lambda smoke test passed.'
