package year2015.day08

import utils.Solution

fun main() {
    with(Day08()) {
        solve()
    }
}

class Day08 : Solution() {
    override val day = 8
    override val year = 2015

    override fun part1(): Any {
        return input.sumOf { line ->
            // Count the non-printable characters
            var count = 2
            var i = 1
            var previousBackslash = false

            while (i < line.lastIndex) {
                val c = line[i]
                var currentBackslash = false

                when (c) {
                    '\\' -> {
                        if (previousBackslash) count += 1 else currentBackslash = true
                    }
                    '"' -> {
                        if (previousBackslash) count += 1 else throw IllegalStateException()
                    }
                    'x' -> {
                        if (previousBackslash) {
                            count += 3
                            i += 2
                        }
                    }
                }

                i += 1
                previousBackslash = currentBackslash
            }

            count
        }
    }

    override fun part2(): Any {
        return input.sumOf { line ->
            // Count the non-printable characters after "encoding" the string
            var count = 4
            var i = 1
            var previousBackslash = false

            while (i < line.lastIndex) {
                val c = line[i]
                var currentBackslash = false

                when (c) {
                    '\\' -> {
                        if (previousBackslash) count += 2 else currentBackslash = true
                    }
                    '"' -> {
                        if (previousBackslash) count += 2 else throw IllegalStateException()
                    }
                    'x' -> {
                        if (previousBackslash) {
                            count += 1
                            i += 2
                        }
                    }
                }

                i += 1
                previousBackslash = currentBackslash
            }

            count
        }
    }
}