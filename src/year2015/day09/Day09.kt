package year2015.day09

import utils.Solution

fun main() {
    with(Day09()) {
        testFile(::part1, 605)
        testFile(::part2, 982)
        solve()
    }
}

private class Day09 : Solution {
    override val day = 9
    override val year = 2015

    override fun part1(input: List<String>): Any {
        val citiesToIndex = input.citiesToIndex()
        val distances = input.parseDistances(citiesToIndex)

        val minDistance = Distance(Int.MAX_VALUE)
        citiesToIndex.forEach { (_, index) ->
            exploreMin(index, 0, mutableSetOf(), distances, minDistance)
        }

        return minDistance.value
    }

    private fun exploreMin(
        city: Int,
        distanceSoFar: Int,
        seen: MutableSet<Int>,
        distances: Array<IntArray>,
        minDistance: Distance
    ) {
        // Pruning by stopping the exploration of paths that go over minDistance
        if(distanceSoFar > minDistance.value) return

        seen.add(city)

        if (seen.size == distances.size) {
            // Every city has been visited, update minDistance if needed
            if (distanceSoFar < minDistance.value) minDistance.value = distanceSoFar
            return
        }

        distances[city].forEachIndexed { otherCity, distance ->
            if (distance != 0 && otherCity !in seen) {
                // This city can be visited and hasn't been visited yet
                exploreMin(otherCity, distanceSoFar + distance, seen, distances, minDistance)
                seen.remove(otherCity)
            }
        }
    }

    override fun part2(input: List<String>): Any {
        val citiesToIndex = input.citiesToIndex()
        val distances = input.parseDistances(citiesToIndex)

        val maxDistance = Distance(0)
        citiesToIndex.forEach { (_, index) ->
            exploreMax(index, 0, mutableSetOf(), distances, maxDistance)
        }

        return maxDistance.value
    }

    private fun exploreMax(
        city: Int,
        distanceSoFar: Int,
        seen: MutableSet<Int>,
        distances: Array<IntArray>,
        maxDistance: Distance
    ) {
        seen.add(city)

        if (seen.size == distances.size) {
            // Every city has been visited, update minDistance if needed
            if (distanceSoFar > maxDistance.value) maxDistance.value = distanceSoFar
            return
        }

        distances[city].forEachIndexed { otherCity, distance ->
            if (distance != 0 && otherCity !in seen) {
                // This city can be visited and hasn't been visited yet
                exploreMax(otherCity, distanceSoFar + distance, seen, distances, maxDistance)
                seen.remove(otherCity)
            }
        }
    }

    private fun List<String>.citiesToIndex() = flatMap { line ->
        val parts = line.split(' ')

        listOf(parts[0], parts[2])
    }.distinct().withIndex().associate { (index, value) -> value to index }

    private fun List<String>.parseDistances(citiesToIndex: Map<String, Int>): Array<IntArray> {
        val distances = Array(citiesToIndex.size) { IntArray(citiesToIndex.size) }

        forEach { line ->
            val parts = line.split(' ')
            val distance = parts[4].toInt()

            distances[citiesToIndex[parts[0]]!!][citiesToIndex[parts[2]]!!] = distance
            distances[citiesToIndex[parts[2]]!!][citiesToIndex[parts[0]]!!] = distance
        }

        return distances
    }

    // Wrapper to pass by reference
    private data class Distance(var value: Int)
}