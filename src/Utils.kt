import java.io.File

fun readInput(name: String) = File("src", "$name.txt").readLines()

fun printResults(
    part1: (List<String>) -> Any,
    part2: (List<String>) -> Any,
    expectedPart1: Any,
    expectedPart2: Any,
    day: Int,
    year: Int
) {
    check(day in 1..25) { "Day $day not in the range 1 to 25"}
    check(year in 2015..2021) { "Year $year not in the range 2015 to 2021"}

    val dayString = day.toString().padStart(2, '0')

    val testInput = readInput("year$year/day$dayString/Day${dayString}_test")
    val input = readInput("year$year/day$dayString/Day${dayString}")

    val testPart1 = part1(testInput)
    val testPart2 = part2(testInput)

    println("Year $year, day $day\n")
    println("\t\t PART 1  ")
    println("TEST:\t\t\t $testPart1 [${if(testPart1 == expectedPart1) "PASS" else "FAIL"}]")
    println("RESULT:\t\t\t ${part1(input)}")
    println()
    println("\t\t PART 2")
    println("TEST:\t\t\t $testPart2 [${if(testPart2 == expectedPart2) "PASS" else "FAIL"}]")
    println("RESULT:\t\t\t ${part2(input)}")
}