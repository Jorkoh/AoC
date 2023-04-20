package year2022.day14

import utils.Solution
import kotlin.math.roundToInt

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
    private data class State(val walls: MutableSet<Coords>, val sand: MutableSet<Coords>)

    override fun part1(): Any {
        val state = State(input.getWalls(), mutableSetOf())
        val lowestWall = state.walls.maxOf { it.y }
        val backStack = ArrayDeque(listOf(Coords(500, 0)))

        while (backStack.isNotEmpty()) {
            val current = backStack.first()
            if (current.y >= lowestWall) break  // Termination condition when it's on free fall

            val down = current.copy(y = current.y + 1)
            val downLeft = down.copy(x = down.x - 1)
            val downRight = down.copy(x = down.x + 1)
            when {
                state.isOpen(down) -> backStack.addFirst(down)
                state.isOpen(downLeft) -> backStack.addFirst(downLeft)
                state.isOpen(downRight) -> backStack.addFirst(downRight)
                else -> {
                    state.sand.add(current)
                    backStack.removeFirst()
                }
            }
        }

        return state.sand.size
    }

    override fun part2(): Any {
        val state = State(input.getWalls(), mutableSetOf())
        val backStack = ArrayDeque(listOf(Coords(500, 0)))

        // ADD THE FLOOR, extends [tan45 * height] each side, +1 to catch the sand
        val floorY = state.walls.maxOf { it.y } + 2
        val side = (1.6198 * floorY).roundToInt() + 1
        state.walls.addAll(((500 - side)..(500 + side)).map { x -> Coords(x, floorY) })

        while (backStack.isNotEmpty()) {
            val current = backStack.first()
            if (current.y < 0) break    // Termination condition when it's blocking the spawn

            val down = current.copy(y = current.y + 1)
            val downLeft = down.copy(x = down.x - 1)
            val downRight = down.copy(x = down.x + 1)
            when {
                state.isOpen(down) -> backStack.addFirst(down)
                state.isOpen(downLeft) -> backStack.addFirst(downLeft)
                state.isOpen(downRight) -> backStack.addFirst(downRight)
                else -> {
                    state.sand.add(current)
                    backStack.removeFirst()
                }
            }
        }

        return state.sand.size
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

    private fun State.isOpen(c: Coords) = c !in sand && c !in walls

    private fun State.printState(c: Coords) {
        val xRange = 480..520
        val yRange = 0..10
        for (y in yRange) {
            for (x in xRange) {
                val c = Coords(x, y)
                print(
                    when (c) {
                        in sand -> 'o'
                        in walls -> '#'
                        c -> '+'
                        else -> ' '
                    }
                )
            }
            println()
        }
    }
}