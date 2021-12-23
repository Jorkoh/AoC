package year2021.day22

import utils.Solution
import java.lang.Long.max
import java.lang.Long.min

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
        val volume = if (x.isEmpty() || y.isEmpty() || z.isEmpty()) {
            0L
        } else {
            (x.last - x.first) * (y.last - y.first) * (z.last - z.first)
        }
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
            println("Zones ${onZones.size}")
            if (type == OperationType.On) onZones.add(newZone)
        }

        return onZones.sumOf { it.volume }
    }

    // Transform the zones depending on newZone to create new zones "around" it
    private fun simplify(newZone: Cuboid, zone: Cuboid): List<Cuboid> {
        val xRanges = buildList {
            if (newZone.x.first > zone.x.first) {
                add(zone.x.first until newZone.x.first)
            }
            if (newZone.x.last + 1 < zone.x.last) {
                add((newZone.x.last + 1)..zone.x.last)
            }
            if (newZone.x.first > zone.x.first || newZone.x.last + 1 < zone.x.last) {
                add(max(zone.x.first, newZone.x.first)..min(zone.x.last, newZone.x.last))
            }
        }
        val yRanges = buildList {
            if (newZone.y.first > zone.y.first) {
                add(zone.y.first until newZone.y.first)
            }
            if (newZone.y.last + 1 < zone.y.last) {
                add((newZone.y.last + 1)..zone.y.last)
            }
            if (newZone.y.first > zone.y.first || newZone.y.last + 1 < zone.y.last) {
                add(max(zone.y.first, newZone.y.first)..min(zone.y.last, newZone.y.last))
            }
        }
        val zRanges = buildList {
            if (newZone.z.first > zone.z.first) {
                add(zone.z.first until newZone.z.first)
            }
            if (newZone.z.last + 1 < zone.z.last) {
                add((newZone.z.last + 1)..zone.z.last)
            }
            if (newZone.z.first > zone.z.first || newZone.z.last + 1 < zone.z.last) {
                add(max(zone.z.first, newZone.z.first)..min(zone.z.last, newZone.z.last))
            }
        }

        return xRanges.map { xRange ->
            yRanges.map { yRange ->
                zRanges.map { zRange ->
                    Cuboid(xRange, yRange, zRange)
                }
            }
        }.flatten().flatten().filterNot { it.x == newZone.x && it.y == newZone.y && it.z == newZone.z }
    }
}