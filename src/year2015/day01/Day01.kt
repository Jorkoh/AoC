package year2015.day01

import utils.Solution

fun main() {
    with(Day01()) {
        test(::part1, 0, listOf("(())"))
        test(::part1, 0, listOf("()()"))
        test(::part1, 3, listOf("((("))
        test(::part1, 3, listOf("(()(()("))
        test(::part1, 3, listOf("))((((("))
        test(::part1, -1, listOf("())"))
        test(::part1, -1, listOf("))("))
        test(::part1, -3, listOf(")))"))
        test(::part1, -3, listOf(")())())"))
        test(::part2, 5, listOf("()())"))
        solve()
    }
}

private class Day01 : Solution {
    override val day = 1
    override val year = 2015

    override fun part1(input: List<String>): Any {
        // https://youtrack.jetbrains.com/issue/KT-46360
        return input.first().sumBy { if (it == '(') 1 else -1 }
    }

    override fun part2(input: List<String>): Any {
        return input.first().foldIndexed(0) { index, previous, c ->
            val current = previous + if (c == '(') 1 else -1
            if (current == -1) return index + 1
            current
        }
    }
}