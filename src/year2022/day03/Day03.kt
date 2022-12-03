package year2022.day03

import utils.Solution

fun main() {
    with(Day03()) {
        testFile(::part1, 157)
        testFile(::part2, 70)
        solve()
    }
}

private class Day03 : Solution {
    override val day = 3
    override val year = 2022

    override fun part1(input: List<String>): Any {
        return input.sumOf { l ->
            val firstHalf = l.take(l.length / 2)
            val secondHalf = l.takeLast(l.length / 2)
            firstHalf.first { it in secondHalf }.asPriority()
        }
    }

    override fun part2(input: List<String>): Any {
        return input.windowed(3, 3) { elves ->
            elves[0].first { it in elves[1] && it in elves[2] }.asPriority()
        }.sum()
    }

    private fun Char.asPriority() = if (this <= 'Z') this - 'A' + 27 else this - 'a' + 1
}
