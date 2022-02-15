package year2015.day02

import utils.Solution

fun main() {
    with(Day02()) {
        testFile(::part1, 101)
        testFile(::part2, 48)
        solve()
    }
}

private class Day02 : Solution {
    override val day = 2
    override val year = 2015

    override fun part1(input: List<String>): Any {
        return input.sumOf { line ->
            val (l, w, h) = line.split('x').map(String::toInt)
            2 * l * w + 2 * w * h + 2 * h * l + minOf(l * w, w * h, h * l)
        }
    }

    override fun part2(input: List<String>): Any {
        return input.sumOf { line ->
            val dims = line.split('x').map(String::toInt)
            dims.sorted().let { it[0] * 2 + it[1] * 2 + it.fold(1) { acc, d -> acc * d } }
        }
    }
}