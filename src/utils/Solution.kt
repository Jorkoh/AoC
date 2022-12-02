package utils

import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

interface Solution {
    val day: Int
    val year: Int

    val dayString
        get() = day.toString().padStart(2, '0')

    fun part1(input: List<String>): Any
    fun part2(input: List<String>): Any

    @OptIn(ExperimentalTime::class)
    fun solve() {
        val input = readInput("year$year/day$dayString/input")

        val part1Result = measureTimedValue { part1(input) }
        val part2Result = measureTimedValue { part2(input) }

        println("\nYear $year, day $day")
        println("PART 1:\t${part1Result.value}\t[${part1Result.duration}]")
        println("PART 2:\t${part2Result.value}\t[${part2Result.duration}]")
    }

    @OptIn(ExperimentalTime::class)
    fun benchmark(runs: Int = 50, preRuns: Int = 300) {
        val input = readInput("year$year/day$dayString/input")

        repeat(preRuns) { part1(input) }
        val part1 = measureTime { repeat(runs) { part1(input) } } / runs
        repeat(preRuns) { part2(input) }
        val part2 = measureTime { repeat(runs) { part2(input) } } / runs

        println("\nBENCHMARK")
        println("PART 1:\t${part1}")
        println("PART 2:\t${part2}")
    }

    fun testFile(part: (List<String>) -> Any, expected: Any, fileName: String = "test") {
        val result = part(readInput("year$year/day$dayString/$fileName"))
        println("[${if (result == expected) "PASS" else "FAIL"}] TEST: $result\tEXPECTED: $expected\t")
    }

    fun test(part: (List<String>) -> Any, expected: Any, input: List<String>) {
        val result = part(input)
        println("[${if (result == expected) "PASS" else "FAIL"}] TEST: $result\tEXPECTED: $expected\t")
    }

    fun test(part: (List<String>) -> Any, expected: Any, input: String) {
        test(part, expected, listOf(input))
    }

    private fun readInput(name: String) = File("src", "$name.txt").readLines()
}