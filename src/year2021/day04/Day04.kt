package year2021.day04

import printResults

fun main() {
    fun part1(input: List<String>): Int {
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

    fun part2(input: List<String>): Int {
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

    printResults(::part1, ::part2, 4512, 1924, 4, 2021)
}

data class Board(val numbers: List<Int>, private val marked: MutableSet<Int> = mutableSetOf()) {
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