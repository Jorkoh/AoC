package year2015.day18

import utils.Solution

fun main() {
    with(Day18()) {
        testFile(::part1, 4, "test1")
        testFile(::part2, 7, "test2")
        solve()
    }
}

class Day18 : Solution() {
    override val day = 18
    override val year = 2015

    override fun part1(): Any {
        var lights = input.map { l -> l.map { it == '#' } }

        repeat(100) {
            lights = lights.mapIndexed { y, row ->
                row.mapIndexed { x, light ->
                    val c = lights.neighbors(x, y).count { it }
                    when {
                        light && (c == 2 || c == 3) -> true
                        !light && c == 3 -> true
                        else -> false
                    }
                }
            }
        }

        return lights.sumOf { l -> l.count { it } }
    }

    override fun part2(): Any {
        var lights = input.map { l -> l.map { it == '#' } }

        repeat(100) {
            lights = lights.mapIndexed { y, row ->
                row.mapIndexed { x, light ->
                    if (lights.isCorner(x, y)) {
                        true // Corners are kept on for part 2
                    } else {
                        val c = lights.neighbors(x, y).count { it }
                        when {
                            light && (c == 2 || c == 3) -> true
                            !light && c == 3 -> true
                            else -> false
                        }
                    }
                }
            }
        }

        return lights.sumOf { l -> l.count { it } }
    }

    private fun List<List<Boolean>>.neighbors(x: Int, y: Int) = listOf(
        if (y == 0) false else this[y - 1][x],
        if (y == 0 || x == first().lastIndex) false else this[y - 1][x + 1],
        if (x == first().lastIndex) false else this[y][x + 1],
        if (x == first().lastIndex || y == lastIndex) false else this[y + 1][x + 1],
        if (y == lastIndex) false else this[y + 1][x],
        if (y == lastIndex || x == 0) false else this[y + 1][x - 1],
        if (x == 0) false else this[y][x - 1],
        if (y == 0 || x == 0) false else this[y - 1][x - 1],
    )

    private fun List<List<Boolean>>.isCorner(x: Int, y: Int) = x == 0 && y == 0
            || x == 0 && y == lastIndex
            || x == first().lastIndex && y == 0
            || x == first().lastIndex && y == lastIndex

    private fun List<List<Boolean>>.print() {
        forEach { l ->
            println(l.map { if (it) '#' else ' ' }.joinToString(""))
        }
    }
}