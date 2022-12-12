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
            isStart = { it == 'S' },
            isEnd = { it == 'E' },
            canMove = { from, to -> to <= from + 1 }
        )
    }

    override fun part2(): Any {
        return shortestPath(
            isStart = { it == 'E' },
            isEnd = { it == 'a' || it == 'S' },
            canMove = { from, to -> from <= to + 1 }
        )
    }

    private fun shortestPath(
        isStart: (Char) -> Boolean,
        isEnd: (Char) -> Boolean,
        canMove: (Char, Char) -> Boolean
    ): Int {
        val start = input.indexOfFirst { it.any(isStart) }.let { y -> input[y].indexOfFirst(isStart) to y }
        var end = -1 to -1
        val (n, m) = input.first().length to input.size
        val positionToCost = mutableMapOf(start to 0)
        val frontier = ArrayDeque(listOf(start))

        while (frontier.isNotEmpty()) {
            val current = frontier.removeFirst()
            if (isEnd(input[current.second][current.first])) {
                end = current
                break
            }
            neighbors(current, n, m).forEach { next ->
                val currentV = input[current.second][current.first].value()
                val nextV = input[next.second][next.first].value()
                if (next !in positionToCost && canMove(currentV, nextV)) {
                    positionToCost[next] = positionToCost[current]!! + 1
                    frontier.addLast(next)
                }
            }
        }

        return positionToCost[end]!!
    }

    private fun Char.value() = when (this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    }

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