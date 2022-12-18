package year2022.day18

import utils.Solution

fun main() {
    with(Day18()) {
        testFile(::part1, 64)
        testFile(::part2, 58)
        solve()
    }
}

class Day18 : Solution() {
    override val day = 18
    override val year = 2022

    private data class Coords(val x: Int, val y: Int, val z: Int)

    override fun part1(): Any {
        val cubes = parseCubes()
        return cubes.sumOf { c -> c.neighbors().count { it !in cubes } }
    }

    override fun part2(): Any {
        val cubes = parseCubes()
        val lowCorner = Coords(
            x = cubes.minOf { it.x } - 1,
            y = cubes.minOf { it.y } - 1,
            z = cubes.minOf { it.z } - 1
        )
        val xRange = lowCorner.x..(cubes.maxOf { it.x } + 1)
        val yRange = lowCorner.y..(cubes.maxOf { it.y } + 1)
        val zRange = lowCorner.z..(cubes.maxOf { it.z } + 1)

        var outerSurface = 0
        val seen = mutableSetOf(lowCorner)
        val frontier = ArrayDeque(listOf(lowCorner))
        while (frontier.isNotEmpty()) {
            val c = frontier.removeFirst()
            val neighbors = c.neighbors()
            outerSurface += neighbors.count { it in cubes }
            neighbors.filter {
                it.x in xRange && it.y in yRange && it.z in zRange && it !in cubes && it !in seen
            }.forEach { neighbor ->
                seen.add(neighbor)
                frontier.addLast(neighbor)
            }
        }

        return outerSurface
    }

    private fun parseCubes() = input.map { l ->
        val (x, y, z) = l.split(',').map(String::toInt)
        Coords(x, y, z)
    }.toSet()

    private fun Coords.neighbors() = buildList {
        add(copy(x = x + 1))
        add(copy(x = x - 1))
        add(copy(y = y + 1))
        add(copy(y = y - 1))
        add(copy(z = z + 1))
        add(copy(z = z - 1))
    }
}
