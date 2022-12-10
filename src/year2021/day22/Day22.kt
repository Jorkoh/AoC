package year2021.day22

import utils.Solution

fun main() {
    with(Day22()) {
        testFile(::part1, 590784, "test1")
        testFile(::part2, 2758514936282235L, "test2")
        solve()
    }
}

class Day22 : Solution() {
    override val day = 22
    override val year = 2021

    private enum class OperationType {
        On, Off
    }

    private data class Cuboid(val x: LongRange, val y: LongRange, val z: LongRange) {
        val volume = if (x.isEmpty() || y.isEmpty() || z.isEmpty()) {
            0L
        } else {
            (x.last - x.first + 1) * (y.last - y.first + 1) * (z.last - z.first + 1)
        }
    }

    private infix fun LongRange.overlaps(that: LongRange) = first <= that.last && last >= that.first

    // NOTE: this code is SUS (ඞ)
    private infix fun Cuboid.overlaps(that: Cuboid) =
        x overlaps that.x && y overlaps that.y && z overlaps that.z

    override fun part1(): Any {
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

    override fun part2(): Any {
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
            val newZones = affectedZones.flatMap { zone -> simplify(newZone, zone) }
            onZones.removeAll(affectedZones)
            onZones.addAll(newZones)
            if (type == OperationType.On) onZones.add(newZone)
        }

        return onZones.sumOf { it.volume }
    }

    private fun simplify(newZone: Cuboid, zone: Cuboid): MutableList<Cuboid> {
        val slice = buildList {
            // X slices
            if (newZone.x.first in zone.x.drop(1))
                add(Cuboid(x = zone.x.first until newZone.x.first, y = zone.y, z = zone.z))
            if (newZone.x.last in zone.x.toList().dropLast(1))
                add(Cuboid(x = (newZone.x.last + 1)..zone.x.last, y = zone.y, z = zone.z))
            // Y slices
            if (newZone.y.first in zone.y.drop(1))
                add(Cuboid(x = zone.x, y = zone.y.first until newZone.y.first, z = zone.z))
            if (newZone.y.last in zone.y.toList().dropLast(1))
                add(Cuboid(x = zone.x, y = (newZone.y.last + 1)..zone.y.last, z = zone.z))
            // Z slices
            if (newZone.z.first in zone.z.drop(1))
                add(Cuboid(x = zone.x, y = zone.y, z = zone.z.first until newZone.z.first))
            if (newZone.z.last in zone.z.toList().dropLast(1))
                add(Cuboid(x = zone.x, y = zone.y, z = (newZone.z.last + 1)..zone.z.last))
        }.maxByOrNull { it.volume } ?: return mutableListOf()

        val remaining = when {
            slice.x != zone.x -> { // X slice
                val remainingX = if (slice.x.first == zone.x.first) (slice.x.last + 1)..zone.x.last
                else zone.x.first until slice.x.first
                Cuboid(x = remainingX, y = zone.y, z = zone.z)
            }
            slice.y != zone.y -> { // Y slice
                val remainingY = if (slice.y.first == zone.y.first) (slice.y.last + 1)..zone.y.last
                else zone.y.first until slice.y.first
                Cuboid(x = zone.x, y = remainingY, z = zone.z)
            }
            slice.z != zone.z -> { // Z slice
                val remainingZ = if (slice.z.first == zone.z.first) (slice.z.last + 1)..zone.z.last
                else zone.z.first until slice.z.first
                Cuboid(x = zone.x, y = zone.y, z = remainingZ)
            }
            else -> throw IllegalStateException()
        }

        return simplify(newZone, remaining).apply { add(slice) }
    }
}