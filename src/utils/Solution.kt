package utils

import java.io.File

interface Solution {
    val day: Int
    val year: Int

    val dayString
        get() = day.toString().padStart(2, '0')

    fun part1(input: List<String>): Any
    fun part2(input: List<String>): Any

    fun solve() {
        val input = readInput("year$year/day$dayString/input")

        println("\nYear $year, day $day")
        println("PART 1:\t${part1(input)}")
        println("PART 2:\t${part2(input)}")
    }

    fun test(part: (List<String>) -> Any, expected: Any, fileName: String = "test") {
        val result = part(readInput("year$year/day$dayString/$fileName"))
        println("[${if (result == expected) "PASS" else "FAIL"}] TEST: $result\tEXPECTED: $expected\t")
    }

    private fun readInput(name: String) = File("src", "$name.txt").readLines()
}