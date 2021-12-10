package year2021.day10

import printResults
import java.util.Stack

fun main() {
    val closeToOpen = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
    val scores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137, '(' to 1, '[' to 2, '{' to 3, '<' to 4)

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.fold(Stack<Char>()) { stack, c ->
                when {
                    c in closeToOpen.values -> stack.push(c) // Open
                    closeToOpen[c] == stack.peek() -> stack.pop() // Close
                    else -> return@sumOf scores[c]!! // Line was corrupted
                }
                stack
            }
            0 // Line was incomplete
        }
    }

    fun part2(input: List<String>): Long {
        return input.mapNotNull { line ->
            line.fold(Stack<Char>()) { stack, c ->
                when {
                    c in closeToOpen.values -> stack.push(c) // Open
                    closeToOpen[c] == stack.peek() -> stack.pop() // Close
                    else -> return@mapNotNull null // Line was corrupted
                }
                stack
            }.foldRight(0L) { c, acc -> acc * 5 + scores[c]!! } // Line was incomplete
        }.sorted().let { it[it.size / 2] }
    }

    printResults(::part1, ::part2, 26397, 288957L, 10, 2021)
}