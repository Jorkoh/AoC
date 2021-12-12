package year2021.day12

import utils.Solution

fun main() {
    with(Day12()) {
        test(::part1, 10, "test1")
        test(::part1, 19, "test2")
        test(::part1, 226, "test3")
        test(::part2, 36, "test1")
        test(::part2, 103, "test2")
        test(::part2, 3509, "test3")
        solve()
    }
}

private class Day12 : Solution {
    override val day = 12
    override val year = 2021

    override fun part1(input: List<String>): Any {
        val adjacencies = buildMap<String, MutableList<String>> {
            input.forEach {
                val (v1, v2) = it.split('-')
                put(v1, getOrDefault(v1, mutableListOf()).apply { add(v2) })
                put(v2, getOrDefault(v2, mutableListOf()).apply { add(v1) })
            }
        }

        return dfs("start", "end", mutableSetOf(), adjacencies)
    }

    override fun part2(input: List<String>): Any {
        val adjacencies = buildMap<String, MutableList<String>> {
            input.forEach {
                val (v1, v2) = it.split('-')
                put(v1, getOrDefault(v1, mutableListOf()).apply { add(v2) })
                put(v2, getOrDefault(v2, mutableListOf()).apply { add(v1) })
            }
        }

        return dfs2("start", "end", mutableMapOf(), adjacencies)
    }

    @Suppress("SameParameterValue")
    private fun dfs(
        start: String,
        end: String,
        visited: MutableSet<String>,
        adjacencies: Map<String, MutableList<String>>
    ): Int {
        if (start in visited && start.toType() != Type.Big) return 0
        if (start == end) return 1

        visited.add(start)
        val paths = adjacencies[start]!!.sumOf { dfs(it, "end", visited, adjacencies) }
        visited.remove(start)

        return paths
    }

    @Suppress("SameParameterValue")
    private fun dfs2(
        start: String,
        end: String,
        visits: MutableMap<String, Int>,
        adjacencies: Map<String, MutableList<String>>
    ): Int {
        if (start.toType() == Type.StartOrEnd && visits[start] == 1 ||
            start.toType() == Type.Small && visits[start] == 2 ||
            start.toType() == Type.Small && visits[start] == 1 &&
            visits.any { it.key.toType() == Type.Small && it.value == 2 }
        ) return 0
        if (start == end) return 1

        visits[start] = visits.getOrDefault(start, 0) + 1
        val paths = adjacencies[start]!!.sumOf { dfs2(it, "end", visits, adjacencies) }
        visits[start] = visits[start]!! - 1

        return paths
    }

    enum class Type {
        Big, Small, StartOrEnd
    }

    private fun String.toType() = when {
        all { it.isUpperCase() } -> Type.Big
        this !in listOf("start", "end") -> Type.Small
        else -> Type.StartOrEnd
    }

    // For learning purposes, with path logging and comments. Pass an empty ArrayDeque for [path] to start
    private fun dfsValidPathsWithPathOutput(
        start: String,
        end: String,
        visited: MutableSet<String>,
        path: ArrayDeque<String>,
        adjacencies: Map<String, MutableList<String>>
    ): Int {
        // Can't continue
        if (start in visited && start.toType() != Type.Big) return 0

        // Found the end
        if (start == end) {
            path.addLast(start)
            println("Found path ${path.joinToString()}\n")
            path.removeLast()
            return 1
        }

        // Mark as visited
        path.addLast(start)
        visited.add(start)

        // Check adjacent vertices
        val pathCount = adjacencies[start]!!.sumOf { dfs(it, "end", visited, adjacencies) }

        // Walking back, unmarked as visited
        path.removeLast()
        visited.remove(start)

        // Return count
        return pathCount
    }
}