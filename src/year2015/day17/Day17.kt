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

private class Day17 : Solution {
    override val day = 17
    override val year = 2015

    var target = 150

    override fun part1(input: List<String>): Any {
        val containers = input.map(String::toInt).sortedDescending()

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

    override fun part2(input: List<String>): Any {
        val containers = input.map(String::toInt).sortedDescending()

        val state = State()
        sumCombinationsWithMin(containers, target, 0, state)

        return state.valid
    }

    class State(var valid: Int = 0, var minContainers: Int = Int.MAX_VALUE)

    private fun sumCombinationsWithMin(containers: List<Int>, remaining: Int, count: Int, s: State) {
        when {
            count > s.minContainers -> return // Overshot the target set by the minimum container combination
            remaining < 0 -> return // Overshot the target
            remaining == 0 -> { // Found a valid combination
                if (count < s.minContainers) {
                    s.minContainers = count
                    s.valid = 0
                }
                s.valid += 1
            }
            else -> containers.withIndex().forEach { (i, container) ->
                sumCombinationsWithMin(
                    containers = containers.drop(i + 1),
                    remaining = remaining - container,
                    count = count + 1,
                    s = s
                )
            }
        }
    }
}