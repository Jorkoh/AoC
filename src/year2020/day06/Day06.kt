package year2020.day06

import utils.Solution

fun main() {
    with(Day06()){
        test(::part1, 11)
        test(::part2, 6)
        solve()
    }
}

private class Day06 : Solution {
    override val day = 6
    override val year = 2020

    override fun part1(input: List<String>): Any {
        return input.joinToString("\n").split("\n\n").sumOf { group ->
            group.toSet().filter { it.isLetter() }.size
        }
    }

    override fun part2(input: List<String>): Any {
        return input.joinToString("\n").split("\n\n").sumOf { group ->
            group.groupingBy { it }.eachCount().count { it.value == group.count { letter -> letter == '\n' } + 1 }
        }
    }
}