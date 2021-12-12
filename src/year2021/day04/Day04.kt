package year2021.day04

import utils.Solution

fun main() {
    with(Day04()) {
        test(::part1, 4512)
        test(::part2, 1924)
        solve()
    }
}

private class Day04 : Solution {
    override val day = 4
    override val year = 2021

    override fun part1(input: List<String>): Int {
        val drawNumbers = input.first().split(",").map { it.toInt() }
        val boards = input.drop(2).windowed(5, 6).map { boardLines ->
            Board(boardLines.flatMap { line -> line.split(" ").mapNotNull { it.toIntOrNull() } })
        }

        drawNumbers.forEach { number ->
            boards.forEach { it.markNumber(number) }
            boards.firstOrNull { it.isWinning() }?.let { return@part1 it.calculateScore() }
        }

        return -1
    }

    override fun part2(input: List<String>): Int {
        val drawNumbers = input.first().split(",").map { it.toInt() }
        val boards = input.drop(2).windowed(5, 6).map { boardLines ->
            Board(boardLines.flatMap { line -> line.split(" ").mapNotNull { it.toIntOrNull() } })
        }

        drawNumbers.fold(boards) { remainingBoards, number ->
            remainingBoards.forEach { it.markNumber(number) }
            val winningBoards = remainingBoards.filter { it.isWinning() }
            if (winningBoards.size == remainingBoards.size) {
                return@part2 winningBoards.last().calculateScore()
            }
            remainingBoards - winningBoards
        }

        return -1
    }
}

private data class Board(val numbers: List<Int>, private val marked: MutableSet<Int> = mutableSetOf()) {
    companion object {
        const val SIZE = 5
    }

    fun markNumber(number: Int) {
        if (number in numbers) marked.add(number)
    }

    fun calculateScore(): Int {
        return numbers.filterNot { it in marked }.sum() * marked.last()
    }

    fun isWinning(): Boolean {
        val hasLine = numbers.chunked(SIZE).any { line -> line.all { it in marked } }
        val hasColumn = (0 until SIZE).any { columnIndex ->
            (0 until SIZE).map { numbers[columnIndex + it * SIZE] }.all { it in marked }
        }

        return hasLine || hasColumn
    }
}