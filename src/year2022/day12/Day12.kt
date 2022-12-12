package year2022.day12

import utils.Solution

fun main() {
    with(Day12()) {
        testFile(::part1, 31)
        testFile(::part2, 29)
        solve()
        benchmark()
    }
}

class Day12 : Solution() {
    override val day = 12
    override val year = 2022

    override fun part1(): Any {

        return shortestPath(
            startChar = 'S',
            endChar = 'E',
            canMove = { from, to -> to <= from + 1 }
        )
    }

    override fun part2(): Any {
        return shortestPath(
            startChar = 'E',
            endChar = 'a',
            canMove = { from, to -> from <= to + 1 }
        )
    }

    private fun shortestPath(
        startChar: Char,
        endChar: Char,
        canMove: (Int, Int) -> Boolean
    ): Int {
        val startY = input.indexOfFirst { it.contains(startChar) }
        val start = input[startY].indexOfFirst { it == startChar } to startY
        var end = -1 to -1
        val mapW = input.first().length
        val mapH = input.size
        val steps = mutableMapOf(start to 0)
        val frontier = ArrayDeque(listOf(start))

        while (frontier.isNotEmpty()) {
            val c = frontier.removeFirst()
            if (input[c.second][c.first] == endChar) {
                end = c
                break
            }
            neighbors(c, mapW, mapH).forEach { n ->
                if (n !in steps && canMove(input[c.second][c.first].value(), input[n.second][n.first].value())) {
                    steps[n] = steps[c]!! + 1
                    frontier.addLast(n)
                }
            }
        }

        return steps[end]!!
    }

    private fun Char.value() = when (this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    } - 'a'

    private fun neighbors(location: Pair<Int, Int>, width: Int, height: Int): List<Pair<Int, Int>> {
        val (x, y) = location
        return buildList(4) {
            if (x > 0) add(x - 1 to y)
            if (x < width - 1) add(x + 1 to y)
            if (y > 0) add(x to y - 1)
            if (y < height - 1) add(x to y + 1)
        }
    }
}