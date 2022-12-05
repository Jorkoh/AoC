package year2022.day04

import utils.Solution

fun main() {
    with(Day04()) {
        testFile(::part1, 2)
        testFile(::part2, 4)
        solve()
    }
}

private class Day04 : Solution {
    override val day = 4
    override val year = 2022

    override fun part1(input: List<String>): Any {
        return input.map(::parse).count { (l1, h1, l2, h2) -> l1 <= l2 && h1 >= h2 || l2 <= l1 && h2 >= h1 }
    }

    override fun part2(input: List<String>): Any {
        return input.map(::parse).count { (l1, h1, l2, h2) -> h1 >= l2 && l1 <= h2 }
    }

    private val regex = Regex("""(\d+)-(\d+),(\d+)-(\d+)""")
    private fun parse(line: String) = regex.find(line)!!.groupValues.drop(1).map(String::toInt)
}
