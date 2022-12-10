package year2021.day01

import utils.Solution

fun main() {
    with(Day01()) {
        testFile(::part1, 7)
        testFile(::part2, 5)
        solve()
    }
}

class Day01 : Solution() {
    override val day = 1
    override val year = 2021

    override fun part1(): Int {
        return input.map { it.toInt() }.windowed(2).count { it.last() > it.first() }
    }

    override fun part2(): Int {
        return input.map { it.toInt() }.windowed(4).count { it.last() > it.first() }
    }
}