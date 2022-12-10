package year2021.day15

import utils.Solution
import java.util.*

fun main() {
    with(Day15()) {
        testFile(::part1, 40)
        testFile(::part2, 315)
        solve()
    }
}

class Day15 : Solution() {
    override val day = 15
    override val year = 2021

    override fun part1(): Any {
        val map = input.map { it.map(Char::digitToInt) }
        val mapW = input.last().length
        val mapH = input.size
        val start = 0 to 0
        val end = mapW - 1 to mapH - 1

        val comparator = Comparator<Pair<Pair<Int, Int>, Int>> { (_, a), (_, b) -> a.compareTo(b) }
        val frontier = PriorityQueue(comparator).apply { add(start to 0) }
        val cameFrom = mutableMapOf(start to start)
        val costSoFar = mutableMapOf(start to 0)

        while (frontier.isNotEmpty()) {
            val (current, _) = frontier.poll()
            if (current == end) break

            neighbors(current, mapW, mapH).forEach { next ->
                val newCost = costSoFar[current]!! + map.costTo(next)
                if (next !in cameFrom || newCost < costSoFar.getOrDefault(next, 0)) {
                    costSoFar[next] = newCost
                    frontier.add(next to newCost)
                    cameFrom[next] = current
                }
            }
        }

        return costSoFar[end]!!
    }

    override fun part2(): Any {
        val map = input.map { it.map(Char::digitToInt) }
        val mapW = input.last().length
        val mapH = input.size
        val start = 0 to 0
        val end = (mapW * 5) - 1 to (mapH * 5) - 1

        val comparator = Comparator<Pair<Pair<Int, Int>, Int>> { (_, a), (_, b) -> a.compareTo(b) }
        val frontier = PriorityQueue(comparator).apply { add(start to 0) }
        val cameFrom = mutableMapOf(start to start)
        val costSoFar = mutableMapOf(start to 0)

        while (frontier.isNotEmpty()) {
            val (current, _) = frontier.poll()
            if (current == end) break

            neighbors(current, mapW * 5, mapH * 5).forEach { next ->
                val dimensionExtra = next.first / mapW + next.second / mapH
                val stepCost = (map.costTo(next.first % mapH to next.second % mapW) + dimensionExtra).let {
                    if (it > 9) it % 10 + 1 else it
                }
                val newCost = costSoFar[current]!! + stepCost
                if (next !in cameFrom || newCost < costSoFar.getOrDefault(next, 0)) {
                    costSoFar[next] = newCost
                    frontier.add(next to newCost)
                    cameFrom[next] = current
                }
            }
        }

        return costSoFar[end]!!
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

    private fun backtracePath(
        cameFrom: Map<Pair<Int, Int>, Pair<Int, Int>>,
        end: Pair<Int, Int>,
        start: Pair<Int, Int>
    ): List<Pair<Int, Int>> {
        var current = end
        val path = mutableListOf<Pair<Int, Int>>()

        while (current != start) {
            path.add(current)
            current = cameFrom[current]!!
        }
        path.add(start)

        return path.reversed()
    }

    private fun List<List<Int>>.costTo(to: Pair<Int, Int>) = this[to.second][to.first]
}