package year2022.day06

import utils.Solution

fun main() {
    with(Day06()) {
        test(::part1, 7, "mjqjpqmgbljsphdztnvjfqwrcgsmlb")
        test(::part1, 5, "bvwbjplbgvbhsrlpgdmjqwftvncz")
        test(::part1, 6, "nppdvjthqldpwncqszvftbrmjlhg")
        test(::part1, 10, "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
        test(::part1, 11, "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")
        test(::part2, 19, "mjqjpqmgbljsphdztnvjfqwrcgsmlb")
        test(::part2, 23, "bvwbjplbgvbhsrlpgdmjqwftvncz")
        test(::part2, 23, "nppdvjthqldpwncqszvftbrmjlhg")
        test(::part2, 29, "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
        test(::part2, 26, "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")
        solve()
    }
}

private class Day06 : Solution {
    override val day = 6
    override val year = 2022

    override fun part1(input: List<String>) = input.first().solve(4)
    override fun part2(input: List<String>) = input.first().solve(14)

    private fun String.solve(size: Int) = windowed(size).indexOfFirst { it.toSet().size == size } + size
}
