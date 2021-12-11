package year2021.day10

import utils.Solution

fun main() {
    with(Day10()) {
        test(::part1, 26397)
        test(::part2, 288957L)
        calculateResults()
    }
}

private class Day10 : Solution {
    override val day = 10
    override val year = 2021

    private val closeToOpen = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
    private val scores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137, '(' to 1, '[' to 2, '{' to 3, '<' to 4)

    override fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.fold(ArrayDeque<Char>()) { stack, c ->
                when {
                    c in closeToOpen.values -> stack.addLast(c) // Open
                    closeToOpen[c] != stack.removeLast() -> return@sumOf scores[c]!! // Line was corrupted
                }
                stack
            }
            0 // Line was incomplete
        }
    }

    override fun part2(input: List<String>): Long {
        return input.mapNotNull { line ->
            line.fold(ArrayDeque<Char>()) { stack, c ->
                when {
                    c in closeToOpen.values -> stack.addLast(c) // Open
                    closeToOpen[c] != stack.removeLast() -> return@mapNotNull null // Line was corrupted
                }
                stack
            }.foldRight(0L) { c, acc -> acc * 5 + scores[c]!! } // Line was incomplete
        }.sorted().let { it[it.size / 2] }
    }
}
