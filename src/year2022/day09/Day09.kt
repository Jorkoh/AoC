package year2022.day09

import utils.Solution
import year2022.day09.Day09.Direction.*
import kotlin.math.abs

fun main() {
    with(Day09()) {
        testFile(::part1, 13)
        testFile(::part2, 1)
        testFile(::part2, 36, "test_bigger")
        solve()
    }
}

class Day09 : Solution() {
    override val day = 9
    override val year = 2022

    private class Rope(length: Int) {
        private val links: MutableList<Position> = MutableList(length) { Position(0, 0) }
        val tailVisited: MutableSet<Position> = mutableSetOf(links.last())

        fun move(direction: Direction, count: Int) {
            repeat(count) {
                // Move the head in the chosen direction
                links[0] = when (direction) {
                    Up -> links[0].copy(y = links[0].y + 1)
                    Down -> links[0].copy(y = links[0].y - 1)
                    Left -> links[0].copy(x = links[0].x - 1)
                    Right -> links[0].copy(x = links[0].x + 1)
                }
                // Adapt the links if needed
                for (i in 1..links.lastIndex) {
                    val p = links[i - 1]
                    val c = links[i]
                    val xD = abs(p.x - c.x)
                    val yD = abs(p.y - c.y)
                    links[i] = Position(
                        x = c.x + if (xD + yD > 2 || xD > 1) if (p.x > c.x) 1 else -1 else 0,
                        y = c.y + if (xD + yD > 2 || yD > 1) if (p.y > c.y) 1 else -1 else 0,
                    )
                    if (i == links.lastIndex) tailVisited.add(links[i])
                }
            }
        }
    }

    private fun Char.toDirection() = when (this) {
        'U' -> Up
        'D' -> Down
        'L' -> Left
        'R' -> Right
        else -> throw IllegalStateException()
    }

    private enum class Direction { Up, Down, Left, Right }
    private data class Position(val x: Int, val y: Int)

    override fun part1(): Any {
        val rope = Rope(2)
        input.forEach { l ->
            rope.move(
                direction = l[0].toDirection(),
                count = l.drop(2).toInt()
            )
        }
        return rope.tailVisited.size
    }

    override fun part2(): Any {
        val rope = Rope(10)
        input.forEach { l ->
            rope.move(
                direction = l[0].toDirection(),
                count = l.drop(2).toInt()
            )
        }
        return rope.tailVisited.size
    }
}
