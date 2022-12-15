package year2022.day15

import utils.Solution
import kotlin.math.abs

fun main() {
    with(Day15()) {
        p = Day15.Params(10, 0, 20)
        testFile(::part1, 26)
        testFile(::part2, 56000011L)
        p = Day15.Params(2000000, 0, 4000000)
        solve()
    }
}

class Day15 : Solution() {
    override val day = 15
    override val year = 2022

    lateinit var p: Params

    data class Params(val tY: Int, val sL: Int, val sH: Int)

    private data class Sensor(val pos: Coords, val beacon: Coords)
    private data class Coords(val x: Int, val y: Int) {
        fun mhDis(b: Coords) = abs(x - b.x) + abs(y - b.y)
    }

    override fun part1(): Any {
        val sensors = input.map { l ->
            val (sX, sY, bX, bY) = l.split('=', ',', ':').mapNotNull(String::toIntOrNull)
            Sensor(Coords(sX, sY), Coords(bX, bY))
        }

        val coveredRanges = sensors.mapNotNull { s ->
            val atY = (s.pos.mhDis(s.beacon) - abs(s.pos.y - p.tY))
            if (atY >= 0) (s.pos.x - atY)..(s.pos.x + atY) else null
        }

        return coveredRanges.simplified().sumOf { it.last - it.first }
    }

    override fun part2(): Any {
        val sensors = input.map { l ->
            val (sX, sY, bX, bY) = l.split('=', ',', ':').mapNotNull(String::toIntOrNull)
            Sensor(Coords(sX, sY), Coords(bX, bY))
        }

        for (y in p.sL..p.sH) {
            val coveredRangesAtY = sensors.mapNotNull { s ->
                val atY = (s.pos.mhDis(s.beacon) - abs(s.pos.y - y))
                if (atY >= 0) (s.pos.x - atY)..(s.pos.x + atY) else null
            }
            coveredRangesAtY.simplified().firstOrNull { r ->
                r.first <= p.sL && r.last >= p.sL && r.last <= p.sH
            }?.let { return (it.last + 1) * 4000000L + y }
        }

        return -1
    }

    private fun List<IntRange>.simplified(): List<IntRange> {
        val simplified = drop(1).fold(listOf(first())) { acc, rr ->
            acc.toMutableList().apply {
                for ((i, sr) in acc.withIndex()) {
                    when {
                        rr.first > sr.last || rr.last < sr.first -> { // Doesn't touch
                            if (i == acc.lastIndex) add(rr)
                        }
                        rr.first < sr.first && rr.last > sr.last -> { // Contains it -> replace
                            set(i, rr)
                            break
                        }
                        rr.first < sr.first && rr.last >= sr.first -> { // Extends to the left
                            set(i, rr.first..sr.last)
                            break
                        }
                        rr.first <= sr.last && rr.last > sr.last -> { // Extends to the right
                            set(i, sr.first..rr.last)
                            break
                        }
                        else -> { // Is contained -> no change
                            break
                        }
                    }
                }
            }
        }
        // Simplification needs to be recursive until it stabilizes
        return if (simplified == this) this else simplified.simplified()
    }
}