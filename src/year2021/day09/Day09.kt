package year2021.day09

import utils.Solution

fun main() {
    with(Day09()) {
        testFile(::part1, 15)
        testFile(::part2, 1134)
        solve()
    }
}

class Day09 : Solution() {
    override val day = 9
    override val year = 2021

    override fun part1(): Int {
        val grid = input.map { it.map(Char::digitToInt) }

        var result = 0
        for (x in grid.first().indices) {
            for (y in grid.indices) {
                if (isLowPoint(x, y, grid)) result += grid[y][x] + 1
            }
        }

        return result
    }

    override fun part2(): Int {
        val grid = input.map { it.map(Char::digitToInt) }

        val basins = mutableListOf<MutableSet<Pair<Int, Int>>>()
        for (x in grid.first().indices) {
            for (y in grid.indices) {
                if (isLowPoint(x, y, grid)) {
                    val basin = mutableSetOf<Pair<Int, Int>>().apply { grow(x, y, grid) }
                    basins.add(basin)
                }
            }
        }

        return basins.map { it.size }.sortedDescending().take(3).reduce { acc, size -> acc * size }
    }

    private fun isLowPoint(x: Int, y: Int, grid: List<List<Int>>): Boolean {
        val current = grid[y][x]
        return (x - 1 < 0 || grid[y][x - 1] > current) &&
                (x + 1 >= grid.first().size || grid[y][x + 1] > current) &&
                (y - 1 < 0 || grid[y - 1][x] > current) &&
                (y + 1 >= grid.size || grid[y + 1][x] > current)
    }

    private fun MutableSet<Pair<Int, Int>>.grow(x: Int, y: Int, grid: List<List<Int>>) {
        // End of basin
        if (x to y in this || x !in grid.first().indices || y !in grid.indices || grid[y][x] == 9) return
        // Mark point as seen
        add(x to y)
        // Explore neighbors
        listOf(x - 1 to y, x + 1 to y, x to y - 1, x to y + 1).forEach { grow(it.first, it.second, grid) }
    }
}
