package year2015.day13

import utils.Solution

fun main() {
    with(Day13_alt()) {
        testFile(::part1, 330)
        solve()
    }
}

private class Day13_alt : Solution {
    override val day = 13
    override val year = 2015

    override fun part1(input: List<String>): Any {
        val peopleToIndex = input.peopleToIndex()
        val happinessTable = input.parseHappiness(peopleToIndex)

        return happinessTable.indices.maxOf { i ->
            bestCombination(i, i, 0, mutableSetOf(), happinessTable)
        }
    }

    private fun bestCombination(
        firstSat: Int,
        lastSat: Int,
        happinessSoFar: Int,
        onTable: MutableSet<Int>,
        relations: Array<IntArray>
    ): Int {
        onTable.add(lastSat)

        return if (onTable.size == relations.size) {
            // Every person is sitting, add the happiness values between the first and the last
            happinessSoFar + relations[firstSat][lastSat] + relations[lastSat][firstSat]
        } else {
            relations.indices.filter { it !in onTable }.maxOf { newSit ->
                val happinessGained = relations[newSit][lastSat] + relations[lastSat][newSit]
                bestCombination(
                    firstSat = firstSat,
                    lastSat = newSit,
                    happinessSoFar = happinessSoFar + happinessGained,
                    onTable = onTable,
                    relations = relations
                ).also { onTable.remove(newSit) }
            }
        }
    }

    override fun part2(input: List<String>): Any {
        val originalPeopleToIndex = input.peopleToIndex().toMutableMap()
        val originalHappinessTable = input.parseHappiness(originalPeopleToIndex)

        val happinessTable = originalHappinessTable.map {
            // Add relationship with me
            it.toMutableList().apply { add(0) }.toIntArray()
        }.toMutableList().apply {
            // Add own relationship with others
            add(IntArray(originalHappinessTable.size + 1))
        }.toTypedArray()

        return happinessTable.indices.maxOf { i ->
            bestCombination(i, i, 0, mutableSetOf(), happinessTable)
        }
    }

    private fun List<String>.peopleToIndex() = map { it.split(' ').first() }
        .distinct()
        .withIndex()
        .associate { (i, v) -> v to i }

    private fun List<String>.parseHappiness(peopleToIndex: Map<String, Int>): Array<IntArray> {
        val distances = Array(peopleToIndex.size) { IntArray(peopleToIndex.size) }

        forEach { line ->
            val parts = line.split(' ')
            val happiness = parts[3].toInt()
            val isNegative = parts[2] == "lose"

            val from = peopleToIndex[parts[0]]!!
            val to = peopleToIndex[parts[parts.lastIndex].dropLast(1)]!!

            distances[from][to] = happiness * if (isNegative) -1 else 1
        }

        return distances
    }
}