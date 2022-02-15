package year2021.day19

import utils.Solution
import kotlin.math.abs

fun main() {
    with(Day19()) {
        testFile(::part1, 79)
        testFile(::part2, 3621)
        solve()
    }
}

private class Day19 : Solution {
    override val day = 19
    override val year = 2021

    private data class Coords(val x: Int, val y: Int, val z: Int) {
        override fun toString(): String {
            return "[$x, $y, $z]"
        }
    }

    private operator fun Coords.minus(b: Coords) = Coords(x - b.x, y - b.y, z - b.z)
    private operator fun Coords.plus(b: Coords) = Coords(x + b.x, y + b.y, z + b.z)

    private data class RotationMatrix(
        val a11: Int, val a21: Int, val a31: Int,
        val a12: Int, val a22: Int, val a32: Int,
        val a13: Int, val a23: Int, val a33: Int,
    ) {
        override fun toString(): String {
            return """
                [${a11.toString().padStart(2)} ${a21.toString().padStart(2)} ${a31.toString().padStart(2)}]
                [${a12.toString().padStart(2)} ${a22.toString().padStart(2)} ${a32.toString().padStart(2)}]
                [${a13.toString().padStart(2)} ${a23.toString().padStart(2)} ${a33.toString().padStart(2)}]
                """.trimIndent()
        }
    }

    private operator fun RotationMatrix.times(that: Coords) = Coords(
        x = a11 * that.x + a21 * that.y + a31 * that.z,
        y = a12 * that.x + a22 * that.y + a32 * that.z,
        z = a13 * that.x + a23 * that.y + a33 * that.z,
    )

    private val rotationMatrices = listOf(
        // x facing positive x and rotating clockwise
        RotationMatrix(1, 0, 0, 0, 1, 0, 0, 0, 1),
        RotationMatrix(1, 0, 0, 0, 0, 1, 0, -1, 0),
        RotationMatrix(1, 0, 0, 0, -1, 0, 0, 0, -1),
        RotationMatrix(1, 0, 0, 0, 0, -1, 0, 1, 0),
        // x facing negative x and rotating clockwise
        RotationMatrix(-1, 0, 0, 0, 1, 0, 0, 0, -1),
        RotationMatrix(-1, 0, 0, 0, 0, -1, 0, -1, 0),
        RotationMatrix(-1, 0, 0, 0, -1, 0, 0, 0, 1),
        RotationMatrix(-1, 0, 0, 0, 0, 1, 0, 1, 0),
        // y facing positive x and rotating clockwise
        RotationMatrix(0, 1, 0, -1, 0, 0, 0, 0, 1),
        RotationMatrix(0, 1, 0, 0, 0, 1, 1, 0, 0),
        RotationMatrix(0, 1, 0, 1, 0, 0, 0, 0, -1),
        RotationMatrix(0, 1, 0, 0, 0, -1, -1, 0, 0),
        // y facing negative x and rotating clockwise
        RotationMatrix(0, -1, 0, -1, 0, 0, 0, 0, -1),
        RotationMatrix(0, -1, 0, 0, 0, -1, 1, 0, 0),
        RotationMatrix(0, -1, 0, 1, 0, 0, 0, 0, 1),
        RotationMatrix(0, -1, 0, 0, 0, 1, -1, 0, 0),
        // z facing positive x and rotating clockwise
        RotationMatrix(0, 0, 1, 0, 1, 0, -1, 0, 0),
        RotationMatrix(0, 0, 1, -1, 0, 0, 0, -1, 0),
        RotationMatrix(0, 0, 1, 0, -1, 0, 1, 0, 0),
        RotationMatrix(0, 0, 1, 1, 0, 0, 0, 1, 0),
        // z facing negative x and rotating clockwise
        RotationMatrix(0, 0, -1, 0, 1, 0, 1, 0, 0),
        RotationMatrix(0, 0, -1, 1, 0, 0, 0, -1, 0),
        RotationMatrix(0, 0, -1, 0, -1, 0, -1, 0, 0),
        RotationMatrix(0, 0, -1, -1, 0, 0, 0, 1, 0)
    )

    private fun commonParseReadings(input: List<String>) = buildMap<String, List<Coords>> {
        var position = 0
        do {
            put(input[position].drop(4).dropLast(4), buildList {
                while (position < input.lastIndex) {
                    val line = input[++position]
                    if (line.isBlank()) {
                        position++
                        break
                    }
                    val (x, y, z) = line.split(',').map(String::toInt)
                    add(Coords(x, y, z))
                }
            })
        } while (position != input.lastIndex)
    }

    private fun commonSolve(
        readings: Map<String, List<Coords>>,
    ): Pair<Set<Pair<String, List<Coords>>>, List<Coords>> {
        val readingsRelativeToFirst = mutableSetOf(readings.entries.first().toPair())
        val scanners = mutableListOf(Coords(0, 0, 0))

        while (readingsRelativeToFirst.size < readings.size) {
            // Keep looping until every reading has been transformed
            for (readingToMatch in readingsRelativeToFirst) {
                // Try to match readingToMatch with one of the remaining readings
                val readingMatched = rotationMatrices.any { rotationMatrix ->
                    // Transform every other scanner result with this matrix
                    val rotatedReadings = readings.filter { reading ->
                        reading.key !in readingsRelativeToFirst.map { it.first }
                    }.mapValues { (_, coords) -> coords.map { rotationMatrix * it } }
                    // Now we need to check if at least 12 of the beacons from one of these rotated readings
                    // match 12 of the beacons from the original scanner reading. If the rotation matrix is
                    // correct the beacons will be offset by the position of the scanner reading them

                    // Find an offset that applied to 12 of the original readings matches 12 of this readings
                    rotatedReadings.entries.any { rotatedReading ->
                        readingToMatch.second.flatMap { ogCoord ->
                            rotatedReading.value.map { coord -> ogCoord - coord }
                        }.groupingBy { it }.eachCount().entries.maxByOrNull { it.value }?.takeIf {
                            it.value >= 12
                        }?.let { (offset, _) ->
                            // A match, save the scanner position and the positions relative to the first
                            scanners.add(offset)
                            readingsRelativeToFirst.add(rotatedReading.key to rotatedReading.value.map { it + offset })
                            true
                        } ?: false
                    }
                }
                // Cringe break to restart the iteration idk
                if (readingMatched) break
            }
        }

        return readingsRelativeToFirst to scanners
    }

    override fun part1(input: List<String>): Any {
        val readings = commonParseReadings(input)

        val (readingsRelativeToFirst, _) = commonSolve(readings)

        return readingsRelativeToFirst.flatMap { it.second }.distinct().size
    }

    override fun part2(input: List<String>): Any {
        val readings = commonParseReadings(input)

        val (_, scanners) = commonSolve(readings)

        return scanners.maxOf { first ->
            scanners.filter { it != first }.maxOf { second ->
                abs(first.x - second.x) + abs(first.y - second.y) + abs(first.z - second.z)
            }
        }
    }
}