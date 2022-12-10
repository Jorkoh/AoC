package utils

import kotlinx.benchmark.Setup
import java.io.File
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

abstract class Solution {
    abstract val day: Int
    abstract val year: Int

    var input: List<String> = emptyList()
        private set

    abstract fun part1(): Any
    abstract fun part2(): Any

    @OptIn(ExperimentalTime::class)
    fun solve() {
        setupInputFromFilename()
        val part1Result = measureTimedValue { part1() }
        val part2Result = measureTimedValue { part2() }

        println("\nYear $year, day $day")
        println("PART 1:\t${part1Result.value}\t[${part1Result.duration}]")
        println("PART 2:\t${part2Result.value}\t[${part2Result.duration}]")
    }

    @OptIn(ExperimentalTime::class)
    fun benchmark(runs: Int = 50, preRuns: Int = 300) {
        setupInputFromFilename()
        repeat(preRuns) { part1() }
        val part1 = measureTime { repeat(runs) { part1() } } / runs
        repeat(preRuns) { part2() }
        val part2 = measureTime { repeat(runs) { part2() } } / runs

        println("\nBENCHMARK")
        println("PART 1:\t${part1}")
        println("PART 2:\t${part2}")
    }

    fun testFile(part: () -> Any, expected: Any, filename: String = "test") {
        setupInputFromFilename(filename)
        val result = part()
        println("[${if (result == expected) "PASS" else "FAIL"}] TEST: $result\tEXPECTED: $expected\t")
    }

    fun test(part: () -> Any, expected: Any, input: List<String>) {
        this.input = input
        val result = part()
        println("[${if (result == expected) "PASS" else "FAIL"}] TEST: $result\tEXPECTED: $expected\t")
    }

    fun test(part: () -> Any, expected: Any, input: String) {
        test(part, expected, listOf(input))
    }

    private fun setupInputFromFilename(name: String = "input") {
        val dayString = day.toString().padStart(2, '0')
        input = File("src", "year$year/day$dayString/$name.txt").readLines()
    }

    @Setup
    fun setupBenchmark() {
        setupInputFromFilename()
    }
}