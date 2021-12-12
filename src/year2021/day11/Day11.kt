package year2021.day11

import utils.Grid
import utils.Grid.Companion.toIntGrid
import utils.Solution

fun main() {
    with(Day11()) {
        test(::part1, 1656)
        test(::part2, 195)
        solve()
    }
}

private class Day11 : Solution {
    override val day = 11
    override val year = 2021

    override fun part1(input: List<String>): Int {
        val grid = input.toIntGrid()
        var flashes = 0

        repeat(100) {
            grid.transformEach { it + 1 }
            grid.forEachIndex { i -> if (grid[i] > 9) flashes += flash(i, grid) }
        }

        return flashes
    }

    override fun part2(input: List<String>): Int {
        val grid = input.toIntGrid()

        repeat(1000) { step ->
            grid.transformEach { it + 1 }
            grid.forEachIndex { coords -> if (grid[coords] > 9) flash(coords, grid) }
            if (grid.all { it == 0 }) return step + 1
        }

        return -1
    }

    private fun flash(ogCoords: Pair<Int, Int>, grid: Grid<Int>): Int {
        grid[ogCoords] = 0
        grid.neighborIndices(ogCoords).forEach { i -> if (grid[i] != 0) grid[i] += 1 }
        return grid.neighborIndices(ogCoords).fold(1) { flashes, i ->
            flashes + if (grid[i] > 9) flash(i, grid) else 0
        }
    }
}
