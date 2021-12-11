package year2021.day01

import utils.Solution

fun main() {
    with(Day01()) {
        test(::part1, 7)
        test(::part2, 5)
        calculateResults()
    }
}

private class Day01 : Solution {
    override val day = 1
    override val year = 2021

    override fun part1(input: List<String>): Int {
        return input.map { it.toInt() }.windowed(2).count { it.last() > it.first() }
    }

    override fun part2(input: List<String>): Int {
        return input.map { it.toInt() }.windowed(4).count { it.last() > it.first() }
    }
}