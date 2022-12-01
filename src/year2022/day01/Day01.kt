package year2022.day01

import utils.Solution

fun main() {
    with(Day01()) {
        testFile(::part1, 24000)
        testFile(::part2, 45000)
        solve()
    }
}

private class Day01 : Solution {
    override val day = 1
    override val year = 2022

    override fun part1(input: List<String>): Int {
        return input.asElfCalories().maxOrNull() ?: 0
    }

    override fun part2(input: List<String>): Int {
        return input.asElfCalories().sortedDescending().take(3).sum()
    }

    private fun List<String>.asElfCalories(): MutableList<Int> {
        val elves = mutableListOf<Int>()

        fold(0) { s, l ->
            if (l.isEmpty()) elves.add(s).run { 0 }
            else s + l.toInt()
        }.also { last -> elves.add(last) }

        return elves
    }
}
