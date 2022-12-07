package year2022.day07

import utils.Solution

fun main() {
    with(Day07()) {
        testFile(::part1, 95437)
        testFile(::part2, 24933642)
        solve()
        benchmark()
    }
}

private class Day07 : Solution {
    override val day = 7
    override val year = 2022

    private data class Dir(
        val name: String,
        val parent: Dir?,
        val subDirs: MutableList<Dir> = mutableListOf(),
        val files: MutableList<File> = mutableListOf()
    )

    private data class File(val name: String, val size: Int)
    private data class DirSize(val name: String, val size: Int)

    override fun part1(input: List<String>): Any {
        val root = input.parseInstructions()

        val dirSizes = mutableListOf<DirSize>()
        calculateDirSizes(root, dirSizes)

        return dirSizes.filter { it.size <= 100000 }.sumOf { it.size }
    }

    override fun part2(input: List<String>): Any {
        val root = input.parseInstructions()

        val dirSizes = mutableListOf<DirSize>()
        calculateDirSizes(root, dirSizes)

        val target = 30000000 - (70000000 - dirSizes.first().size)
        return dirSizes.filter { it.size > target }.minOf { it.size }
    }

    private fun List<String>.parseInstructions(): Dir {
        val root = Dir(name = "/", parent = null)
        var active = root
        forEach { cmd ->
            val parts = cmd.split(" ")
            when {
                parts[0] == "$" && parts[1] == "cd" -> { // Change directory
                    val name = parts[2]
                    active = when (name) {
                        "/" -> root
                        ".." -> active.parent!!
                        else -> active.subDirs.first { it.name == name }
                    }
                }
                parts[0] == "$" && parts[1] == "ls" -> { // List
                    // Do nothing, we'll just add each one as we read them
                }
                parts[0] == "dir" -> { // Listed directory
                    val added = Dir(name = parts[1], parent = active)
                    active.subDirs.add(added)
                }
                else -> { // Listed file
                    active.files.add(File(name = parts[1], size = parts[0].toInt()))
                }
            }
        }
        return root
    }

    private fun calculateDirSizes(current: Dir, dirSizes: MutableList<DirSize>) {
        dirSizes.add(DirSize(current.name, current.getSize()))
        current.subDirs.forEach { dir -> calculateDirSizes(dir, dirSizes) }
    }

    private fun Dir.getSize(): Int = files.sumOf { it.size } + subDirs.sumOf { it.getSize() }
}
