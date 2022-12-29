package year2022.day17

import utils.Solution

fun main() {
    with(Day17()) {
        testFile(::part1, Unit)
    }
}

class Day17 : Solution() {
    override val day = 17
    override val year = 2022

    private data class Co(val x: Int, val y: Int)
    private data class Shape(val coords: List<Co>)

    override fun part1(): Any {
        val shapes = listOf(
            // Horizontal line
            Shape(listOf(Co(0, 0), Co(1, 0), Co(2, 0), Co(3, 0), Co(4, 0))),
            // Plus sign
            Shape(listOf(Co(1, 0), Co(0, 1), Co(1, 1), Co(2, 1), Co(1, 2))),
            // Mirror L
            Shape(listOf(Co(2, 0), Co(2, 1), Co(0, 2), Co(1, 2), Co(2, 2))),
            // Vertical line
            Shape(listOf(Co(0, 0), Co(0, 1), Co(0, 2), Co(0, 3))),
            // Square
            Shape(listOf(Co(0, 0), Co(1, 0), Co(0, 1), Co(1, 1)))
        )
        var turn = 0
        while (true) {
            // Be pushed
            val wind = input[turn % input.size]
            // Fall down
            turn++
        }
        return Unit
    }

    override fun part2(): Any {
        return Unit
    }
}