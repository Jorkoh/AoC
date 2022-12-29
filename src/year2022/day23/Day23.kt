package year2022.day23

import utils.Solution
import year2022.day23.Day23.Directions.*

fun main() {
    with(Day23()) {
        testFile(::part1, 110)
        testFile(::part2, 20)
        solve()
    }
}

class Day23 : Solution() {
    override val day = 23
    override val year = 2022

    private enum class Directions { N, S, W, E }
    private data class Coord(val x: Int, val y: Int)

    override fun part1(): Any {
        val elves = parseElves()

        repeat(10) { turn ->
            val propToElves = mutableMapOf<Coord, MutableList<Int>>()
            val elfToProp = mutableMapOf<Int, Coord>()
            elves.firstHalf(turn, propToElves, elfToProp)
            elves.secondHalf(propToElves, elfToProp)
        }

        val minX = elves.minOf { it.key.x }
        val minY = elves.minOf { it.key.y }
        val maxX = elves.maxOf { it.key.x }
        val maxY = elves.maxOf { it.key.y }
        return ((maxX - minX + 1) * (maxY - minY + 1)) - elves.size // Area minus occupied
    }

    override fun part2(): Any {
        val elves = parseElves()

        repeat(Int.MAX_VALUE) { turn ->
            val propToElves = mutableMapOf<Coord, MutableList<Int>>()
            val elfToProp = mutableMapOf<Int, Coord>()
            elves.firstHalf(turn, propToElves, elfToProp)
            if (propToElves.isEmpty()) return turn + 1 // If no one wants to move at this point we are done
            elves.secondHalf(propToElves, elfToProp)
        }

        return -1
    }

    private fun parseElves() = mutableMapOf<Coord, Int>().apply {
        for ((y, r) in input.withIndex()) {
            for ((x, c) in r.withIndex()) {
                if (c == '#') put(Coord(x, y), size)
            }
        }
    }

    private fun Map<Coord, Int>.firstHalf(
        turn: Int,
        propToElves: MutableMap<Coord, MutableList<Int>>,
        elfToProp: MutableMap<Int, Coord>
    ) {
        for (elf in this) {
            val neighbors = elf.key.neighbors()
            if (neighbors.none { it in this }) continue // Doesn't need to move
            for (offset in turn..turn + 3) {
                val (toCheck, prop) = when (values()[offset % 4]) {
                    N -> listOf(neighbors[0], neighbors[1], neighbors[7]) to neighbors[0]
                    S -> listOf(neighbors[3], neighbors[4], neighbors[5]) to neighbors[4]
                    W -> listOf(neighbors[5], neighbors[6], neighbors[7]) to neighbors[6]
                    E -> listOf(neighbors[1], neighbors[2], neighbors[3]) to neighbors[2]
                }
                if (toCheck.none { it in this }) {
                    // If the surrounding are empty propose the destination
                    propToElves.getOrPut(prop) { mutableListOf() } += elf.value
                    elfToProp[elf.value] = prop
                    break
                }
            }
        }
    }

    private fun MutableMap<Coord, Int>.secondHalf(
        propToElves: MutableMap<Coord, MutableList<Int>>,
        elfToProp: MutableMap<Int, Coord>
    ) {
        val oldElves = entries.toMutableList()
        for (elf in oldElves) {
            val prop = elfToProp[elf.value]
            if (prop != null && propToElves[prop]!!.size == 1) {
                // If it proposed a destination, and he was the only one to do so move him
                remove(elf.key)
                this[prop] = elf.value
            }
        }
    }

    // Starting N and clockwise
    private fun Coord.neighbors() = listOf(
        Coord(x = x, y = y - 1),     // N
        Coord(x = x + 1, y = y - 1), // NE
        Coord(x = x + 1, y = y),     // E
        Coord(x = x + 1, y = y + 1), // SE
        Coord(x = x, y = y + 1),     // S
        Coord(x = x - 1, y = y + 1), // SW
        Coord(x = x - 1, y = y),     // W
        Coord(x = x - 1, y = y - 1), // NW
    )

    private fun Map<Coord, Int>.print() {
        val minX = minOf { it.key.x }
        val minY = minOf { it.key.y }
        val maxX = maxOf { it.key.x }
        val maxY = maxOf { it.key.y }

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(if (Coord(x, y) in this) '█' else '.')
            }
            println()
        }
    }
}