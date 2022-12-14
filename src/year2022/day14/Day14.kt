package year2022.day14

import utils.Solution
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {
    with(Day14()) {
        testFile(::part1, 24)
        testFile(::part2, 93)
        solve()
        benchmark()
    }
}

class Day14 : Solution() {
    override val day = 14
    override val year = 2022

    private data class Coords(val x: Int, val y: Int)

    override fun part1(): Any {
        val walls = input.getWalls()
        val sand = mutableSetOf<Coords>()

        var freeFalling = false
        while (!freeFalling) { // Overall simulation ends on free fall
            var grain = Coords(500, 0)
            var steps = 0
            var resting = false
            while (!resting && !freeFalling) { // Simulate until rest or free fall
                val down = grain.copy(y = grain.y + 1)
                val downLeft = grain.copy(x = grain.x - 1, y = grain.y + 1)
                val downRight = grain.copy(x = grain.x + 1, y = grain.y + 1)
                when {
                    down.isOpen(walls, sand) -> grain = down
                    downLeft.isOpen(walls, sand) -> grain = downLeft
                    downRight.isOpen(walls, sand) -> grain = downRight
                    else -> resting = true
                }

                if (steps++ > 2000) freeFalling = true
            }
            sand.add(grain.copy())
        }

        return sand.size - 1 // Last grain is lost in the abyss, don't count it
    }

    override fun part2(): Any {
        val walls = input.getWalls()
        val sand = mutableSetOf<Coords>()

        // ADD THE FLOOR
        val side = (2 * 500 / (sqrt(3.0))).roundToInt()
        val floorMinX = walls.minOf { it.x } - side / 2
        val floorMaxX = walls.maxOf { it.x } + side / 2
        val floorY = walls.maxOf { it.y } + 2
        walls.addAll((floorMinX..floorMaxX).map { x -> Coords(x, floorY) })

        while (Coords(500, 0).isOpen(walls, sand)) { // Overall simulation ends when source gets blocked
            var grain = Coords(500, 0)
            var resting = false
            while (!resting) { // Simulate until rest
                val down = grain.copy(y = grain.y + 1)
                val downLeft = grain.copy(x = grain.x - 1, y = grain.y + 1)
                val downRight = grain.copy(x = grain.x + 1, y = grain.y + 1)
                when {
                    down.isOpen(walls, sand) -> grain = down
                    downLeft.isOpen(walls, sand) -> grain = downLeft
                    downRight.isOpen(walls, sand) -> grain = downRight
                    else -> resting = true
                }
            }
            sand.add(grain.copy())
        }

        return sand.size
    }

    private fun List<String>.getWalls() = this.flatMap { l ->
        val points = l.split(" -> ").map {
            val (x, y) = it.split(',').map(String::toInt)
            Coords(x, y)
        }
        points.windowed(2).flatMap { (a, b) ->
            when {
                a.x == b.x && a.y <= b.y -> (a.y..b.y).map { y -> Coords(a.x, y) }
                a.x == b.x && a.y > b.y -> (b.y..a.y).map { y -> Coords(a.x, y) }
                a.y == b.y && a.x <= b.x -> (a.x..b.x).map { x -> Coords(x, a.y) }
                else -> (b.x..a.x).map { x -> Coords(x, a.y) }
            }
        }
    }.toMutableSet()

    private fun Coords.isOpen(walls: Set<Coords>, sand: Set<Coords>) = this !in sand && this !in walls
}