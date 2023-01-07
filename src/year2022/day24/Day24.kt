package year2022.day24

import utils.Solution

fun main() {
    with(Day24()) {
        testFile(::part1, 18)
        testFile(::part2, 54)
        solve()
    }
}

// Dijkstra was slower than plain BFS
class Day24 : Solution() {
    override val day = 24
    override val year = 2022

    private data class Coords(val x: Int, val y: Int)
    private enum class Dir { Up, Down, Left, Right }

    private data class Part1State(
        val coords: Coords,
        val turn: Int
    )

    override fun part1(): Any {
        var blizzards = parseBlizzards()
        var bT = 0
        val w = input.first().length
        val h = input.size
        val target = Coords(w - 2, h - 1)
        val seen = mutableSetOf<Part1State>()

        val frontier = ArrayDeque(listOf(Part1State(Coords(1, 0), 0)))
        while (frontier.isNotEmpty()) {
            val (c, t) = frontier.removeFirst()
            if (c == target) return t // End condition
            if (t == bT) { // Blizzards need to be ahead to calculate valid options
                blizzards = blizzards.moveBlizzards(w, h)
                bT++
            }
            for (option in c.options(blizzards, w, h)) {
                val state = Part1State(coords = option, turn = t + 1)
                if (state !in seen) {
                    frontier.addLast(state)
                    seen.add(state)
                }
            }
        }

        return -1
    }

    private data class Part2State(
        val coords: Coords,
        val turn: Int,
        val hitEnd: Boolean,
        val hitStartAfterEnd: Boolean
    )

    override fun part2(): Any {
        var blizzards = parseBlizzards()
        var bT = 0
        val w = input.first().length
        val h = input.size
        val start = Coords(1, 0)
        val end = Coords(w - 2, h - 1)
        val seen = mutableSetOf<Part2State>()

        val frontier = ArrayDeque(listOf(Part2State(start, 0, hitEnd = false, hitStartAfterEnd = false)))
        while (frontier.isNotEmpty()) {
            val (c, t, hitEnd, hitStartAfterEnd) = frontier.removeFirst()
            if (c == end && hitStartAfterEnd) return t
            if (t == bT) { // Blizzards need to be ahead to calculate valid options
                blizzards = blizzards.moveBlizzards(w, h)
                bT++
            }
            for (option in c.options(blizzards, w, h)) {
                val state = Part2State(
                    coords = option,
                    turn = t + 1,
                    hitEnd = hitEnd || c == end,
                    hitStartAfterEnd = hitStartAfterEnd || hitEnd && c == start
                )
                if (state !in seen) {
                    frontier.addLast(state)
                    seen.add(state)
                }
            }
        }

        return -1
    }

    private fun parseBlizzards(): Map<Coords, List<Dir>> {
        val blizzards = mutableMapOf<Coords, MutableList<Dir>>()
        input.forEachIndexed { y, r ->
            r.forEachIndexed { x, c ->
                when (c) {
                    '^' -> Dir.Up
                    'v' -> Dir.Down
                    '<' -> Dir.Left
                    '>' -> Dir.Right
                    else -> null
                }?.let {
                    blizzards.getOrPut(Coords(x, y)) { mutableListOf() } += it
                }
            }
        }
        return blizzards
    }

    /**
     * Allowed to wait in place or move up, down, left or right
     */
    private fun Coords.options(blizzards: Map<Coords, List<Dir>>, w: Int, h: Int): List<Coords> = buildList {
        var nP = copy()
        if (blizzards[nP] == null && nP.isInBounds(w, h)) add(nP)
        nP = copy(x = x + 1)
        if (blizzards[nP] == null && nP.isInBounds(w, h)) add(nP)
        nP = copy(x = x - 1)
        if (blizzards[nP] == null && nP.isInBounds(w, h)) add(nP)
        nP = copy(y = y + 1)
        if (blizzards[nP] == null && nP.isInBounds(w, h)) add(nP)
        nP = copy(y = y - 1)
        if (blizzards[nP] == null && nP.isInBounds(w, h)) add(nP)
    }

    private fun Coords.isInBounds(w: Int, h: Int) =
        x > 0 && x < w - 1 && y > 0 && y < h - 1 || x == 1 && y == 0 || x == w - 2 && y == h - 1

    /**
     * Blizzards wrap round when they reach the edge
     */
    private fun Map<Coords, List<Dir>>.moveBlizzards(w: Int, h: Int): Map<Coords, List<Dir>> {
        val newBlizzards = mutableMapOf<Coords, MutableList<Dir>>()
        forEach { (coords, dirs) ->
            for (dir in dirs) {
                val newCoords = when (dir) {
                    Dir.Up -> Coords(coords.x, coords.y - 1)
                    Dir.Down -> Coords(coords.x, coords.y + 1)
                    Dir.Left -> Coords(coords.x - 1, coords.y)
                    Dir.Right -> Coords(coords.x + 1, coords.y)
                }.let { preWrap ->
                    when {
                        preWrap.x == 0 -> preWrap.copy(x = w - 2)
                        preWrap.x == w - 1 -> preWrap.copy(x = 1)
                        preWrap.y == 0 -> preWrap.copy(y = h - 2)
                        preWrap.y == h - 1 -> preWrap.copy(y = 1)
                        else -> preWrap
                    }
                }
                newBlizzards.getOrPut(newCoords) { mutableListOf() } += dir
            }
        }
        return newBlizzards
    }
}