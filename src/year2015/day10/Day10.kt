package year2015.day10

import utils.Solution

fun main() {
    with(Day10()) {
        solve()
    }
}

private class Day10 : Solution {
    override val day = 10
    override val year = 2015

    override fun part1(input: List<String>): Any {
        var line = input.first()

        repeat(40) { line = lookAndSay(line) }

        return line.length
    }

    override fun part2(input: List<String>): Any {
        var line = input.first()

        repeat(50) { line = lookAndSay(line) }

        return line.length
    }

    private fun lookAndSay(line: String) = buildString {
        var count = 1

        for (i in 1..line.lastIndex) {
            val current = line[i]
            val previous = line[i - 1]

            if (current == previous) {
                count += 1
            } else {
                append(count)
                append(previous)
                count = 1
            }
        }

        append(count)
        append(line.last())
    }
}