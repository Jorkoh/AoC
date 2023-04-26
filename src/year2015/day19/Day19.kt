package year2015.day19

import utils.Solution

fun main() {
    with(Day19()) {
        testFile(::part1, 7, "test1")
        testFile(::part2, 6, "test2")
        solve()
    }
}

class Day19 : Solution() {
    override val day = 19
    override val year = 2015

    override fun part1(): Any {
        val replacements = input.dropLast(2).map {
            val (from, to) = it.split(" => ")
            from to to
        }
        val start = input.last()
        val molecules = mutableSetOf<String>()

        for ((from, to) in replacements) {
            var checked = 0
            while (true) {
                val pos = start.indexOf(from, checked)
                if (pos == -1) break // This replacement can't be applied any further

                molecules.add("${start.take(pos)}$to${start.drop(pos + from.length)}")
                checked = pos + from.length
            }
        }

        return molecules.size
    }


    override fun part2(): Any {
        val start = "e"
        val target = input.last()
        val replacements = input.dropLast(2).map {
            val (from, to) = it.split(" => ")
            from to to
        }

        // Naive DFS gives the valid answer really quick but letting it exhaust the entire tree
        // to prove that it's the most optimal would take forever. Checking the solution's thread
        // it seems like the "proper" answer was to find specific rules on the input or implement CYK
        // BFS with some heuristics like greedily choosing the biggest substitution is an alternative
        minSteps = Int.MAX_VALUE
        findMinSteps(
            molecule = target,
            target = start,
            replacements = replacements,
            steps = 0
        )

        return minSteps
    }

    private var minSteps = Int.MAX_VALUE

    private fun findMinSteps(
        molecule: String,
        target: String,
        replacements: List<Pair<String, String>>,
        steps: Int
    ) {
        if (steps >= minSteps) return // Prune branches that can't be better than what we got
        if (molecule == target) { // Got a solution that improves over the current one
            println(steps) // it won't explore the entire thing in a reasonable amount of time...
            minSteps = steps
            return
        }

        for ((to, from) in replacements) {
            var checked = 0
            while (true) {
                val pos = molecule.indexOf(from, checked)
                if (pos == -1) break // This replacement can't be applied any further

                checked = pos + from.length
                findMinSteps(
                    molecule = "${molecule.take(pos)}$to${molecule.drop(pos + from.length)}",
                    target = target,
                    replacements = replacements,
                    steps = steps + 1
                )
            }
        }
    }
}
