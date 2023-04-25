package year2015.day17

import utils.Solution

fun main() {
    with(Day17()) {
        target = 25
        testFile(::part1, 4)
        testFile(::part2, 3)
        target = 150
        solve()
    }
}

class Day17 : Solution() {
    override val day = 17
    override val year = 2015

    var target = 150

    override fun part1(): Any {
        val containers = input.map(String::toInt)

        return sumCombinations(containers, target)
    }

    private fun sumCombinations(containers: List<Int>, remaining: Int): Int {
        return when {
            remaining == 0 -> 1 // Found a valid combination
            remaining < 0 -> 0 // Overshot the target
            else -> {
                containers.withIndex().sumOf { (i, container) ->
                    sumCombinations(
                        containers = containers.drop(i + 1),
                        remaining = remaining - container
                    )
                }
            }
        }
    }

    override fun part2(): Any {
        val containers = input.map(String::toInt)

        val state = Part2State()
        sumCombinationsWithMin(containers, target, 0, state)

        return state.countWithMinContainers
    }

    private class Part2State(var countWithMinContainers: Int = 0, var minContainers: Int = Int.MAX_VALUE)

    private fun sumCombinationsWithMin(
        containers: List<Int>,
        remaining: Int,
        containersUsed: Int,
        s: Part2State
    ) {
        when {
            containersUsed > s.minContainers -> return // Overshot the target set by the minimum container combination
            remaining < 0 -> return // Overshot the target
            remaining == 0 -> { // Found a valid combination
                if (containersUsed < s.minContainers) { // It forms a new minimum
                    s.minContainers = containersUsed
                    s.countWithMinContainers = 0 // Reset the count since there's a new minimum
                }
                s.countWithMinContainers += 1
            }
            else -> containers.withIndex().forEach { (i, container) ->
                sumCombinationsWithMin(
                    containers = containers.drop(i + 1),
                    remaining = remaining - container,
                    containersUsed = containersUsed + 1,
                    s = s
                )
            }
        }
    }
}