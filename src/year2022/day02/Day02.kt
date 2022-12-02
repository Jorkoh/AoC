package year2022.day02

import utils.Solution

fun main() {
    with(Day02()) {
        testFile(::part1, 15)
        testFile(::part2, 12)
        solve()
        benchmark()
    }
}

private class Day02 : Solution {
    override val day = 2
    override val year = 2022

    val part1 = listOf(
        listOf(3 + 1, 6 + 2, 0 + 3),
        listOf(0 + 1, 3 + 2, 6 + 3),
        listOf(6 + 1, 0 + 2, 3 + 3),
    )

    val part2 = listOf(
        listOf(0 + 3, 3 + 1, 6 + 2),
        listOf(0 + 1, 3 + 2, 6 + 3),
        listOf(0 + 2, 3 + 3, 6 + 1),
    )

    override fun part1(input: List<String>) = input.sumOf { l -> part1[l[0] - 'A'][l[2] - 'X'] }
    override fun part2(input: List<String>) = input.sumOf { l -> part2[l[0] - 'A'][l[2] - 'X'] }
}