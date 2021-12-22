package year2021.day22

import utils.Solution

fun main() {
    with(Day22()) {
        test(::part1, 590784, "test1")
        test(::part2, 2758514936282235L, "test2")
        solve()
    }
}

private class Day22 : Solution {
    override val day = 22
    override val year = 2021

    private enum class OperationType {
        On, Off
    }

    private data class Cuboid(
        val x: LongRange,
        val y: LongRange,
        val z: LongRange
    ) {
        val volume = (x.last - x.first) * (y.last - y.first) * (z.last - z.first)
    }

    private infix fun LongRange.overlaps(that: LongRange) = first <= that.last && last >= that.first

    private infix fun LongRange.contains(that: LongRange) = first <= that.first && last >= that.last

    // NOTE: this code is SUS (ඞ)
    private infix fun Cuboid.overlaps(that: Cuboid) =
        x overlaps that.x && y overlaps that.y && z overlaps that.z

    // NOTE: this code is also SUS (ඞ)
    private infix fun Cuboid.contains(that: Cuboid) =
        x contains that.x && y contains that.y && z contains that.z

    override fun part1(input: List<String>): Any {
        val operations = input.map { line ->
            val (operationString, rangesString) = line.split(' ')
            val type = if (operationString[1] == 'n') OperationType.On else OperationType.Off
            val ranges = rangesString.split(',').map {
                val (start, end) = it.drop(2).split("..").map(String::toLong)
                start..end
            }
            Pair(type, Cuboid(ranges[0], ranges[1], ranges[2]))
        }

        val cubes = Array(101) { Array(101) { Array(101) { false } } }
        val zoneOfInterest = Cuboid(-50L..50L, -50L..50L, -50L..50L)

        operations.forEach { (type, zone) ->
            if (zone overlaps zoneOfInterest) {
                zone.x.forEach { x ->
                    zone.y.forEach { y ->
                        zone.z.forEach { z ->
                            cubes[(x + 50).toInt()][(y + 50).toInt()][(z + 50).toInt()] = type == OperationType.On
                        }
                    }
                }
            }
        }

        return cubes.sumOf { column -> column.sumOf { row -> row.count { it } } }
    }

    override fun part2(input: List<String>): Any {
        val operations = input.map { line ->
            val (operationString, rangesString) = line.split(' ')
            val type = if (operationString[1] == 'n') OperationType.On else OperationType.Off
            val ranges = rangesString.split(',').map {
                val (start, end) = it.drop(2).split("..").map(String::toLong)
                start..end
            }
            Pair(type, Cuboid(ranges[0], ranges[1], ranges[2]))
        }

        val onZones = mutableListOf<Cuboid>()
        operations.forEach { (type, newZone) ->
            val affectedZones = onZones.filter { it overlaps newZone }
            val newZones = affectedZones.map { zone -> simplify(newZone, zone) }.flatten()

            onZones.removeAll(affectedZones)
            onZones.addAll(newZones)
            if (type == OperationType.On) onZones.add(newZone)
        }

        return onZones.sumOf { it.volume }
    }

    // Transform the zones depending on newZone to create new zones "around" it
    private fun simplify(newZone: Cuboid, zone: Cuboid): List<Cuboid> {
        val containedEdges = listOf(
            newZone.x contains zone.x,
            newZone.y contains zone.y,
            newZone.z contains zone.z
        ).count { it }
        return when {
            newZone contains zone -> emptyList()
            zone contains newZone -> {
                listOf(
                    Cuboid(zone.x, zone.y, zone.z.first until newZone.z.first),
                    Cuboid(zone.x, zone.y, (newZone.z.last + 1)..newZone.z.last),
                    Cuboid(zone.x.first until newZone.x.first, zone.y, newZone.z.first..newZone.z.last),
                    Cuboid((newZone.x.last + 1)..zone.x.last, zone.y, newZone.z.first..newZone.z.last)
                )
            }
            containedEdges == 2 -> {
                val x = when {
                    newZone.x contains zone.x -> zone.x
                    zone.x.first < newZone.x.first -> zone.x.first until newZone.x.first
                    else -> (newZone.x.last + 1)..zone.x.last
                }
                val y = when {
                    newZone.y contains zone.y -> zone.y
                    zone.y.first < newZone.y.first -> zone.y.first until newZone.y.first
                    else -> (newZone.y.last + 1)..zone.y.last
                }
                val z = when {
                    newZone.z contains zone.z -> zone.z
                    zone.z.first < newZone.z.first -> zone.z.first until newZone.z.first
                    else -> (newZone.z.last + 1)..zone.z.last
                }
                listOf(Cuboid(x, y, z))
            }
            containedEdges == 1 -> {
                // should result in 2
                when {
                    newZone.x contains zone.x -> listOf(
                        Cuboid(
                            zone.x,
                            if(zone.y.first < newZone.y.first) zone.y.first until newZone.y.first
                            else (newZone.y.last + 1)..zone.y.last,
                            zone.z
                        ),
                        Cuboid(
                            zone.x,
                            if(zone.y.first < newZone.y.first) newZone.y.first..zone.y.last
                            else zone.y.first..newZone.y.last,
                            if(zone.z.first < newZone.z.first) zone.z.first until newZone.z.first
                            else (newZone.z.last + 1)..zone.z.last
                        )
                    )
                    newZone.y contains zone.y -> listOf(
                        Cuboid(
                            if(zone.x.first < newZone.x.first) zone.x.first until newZone.x.first
                            else (newZone.x.last + 1)..zone.x.last,
                            zone.y,
                            zone.z
                        ),
                        Cuboid(
                            if(zone.x.first < newZone.x.first) newZone.x.first..zone.x.last
                            else zone.x.first..newZone.x.last,
                            zone.y,
                            if(zone.z.first < newZone.z.first) zone.z.first until newZone.z.first
                            else (newZone.z.last + 1)..zone.z.last
                        )
                    )
                    newZone.z contains zone.z -> listOf(
                        Cuboid(
                            if(zone.x.first < newZone.x.first) zone.x.first until newZone.x.first
                            else (newZone.x.last + 1)..zone.x.last,
                            zone.y,
                            zone.z
                        ),
                        Cuboid(
                            if(zone.x.first < newZone.x.first) newZone.x.first..zone.x.last
                            else zone.x.first..newZone.x.last,
                            if(zone.y.first < newZone.y.first) zone.y.first until newZone.y.first
                            else (newZone.y.last + 1)..zone.y.last,
                            zone.z
                        )
                    )
                    else -> throw IllegalStateException()
                }
            }
            containedEdges == 0 -> {
                // multiple cases here
                emptyList<Cuboid>()
            }
            else -> throw IllegalStateException()
        }
    }
}