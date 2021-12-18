package year2021.day18

import utils.Solution
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    with(Day18()) {
        test(::part1, 4140)
        test(::part2, 3993)
         solve()
    }
}

private class Day18 : Solution {
    override val day = 18
    override val year = 2021

    class Node(
        var value: Int? = null,
        var left: Node? = null,
        var right: Node? = null,
        var parent: Node? = null
    ) {
        val isValue
            get() = value != null

        fun putChildren(node: Node) {
            when {
                left == null -> left = node
                right == null -> right = node
                else -> throw IllegalStateException("Tried to add children to node with both sides full")
            }
        }
    }

    private operator fun Node.plus(that: Node) = Node(
        left = this,
        right = that
    ).also {
        this.parent = it
        that.parent = it
    }

    private fun Node.reduce(): Node {
        do {
            while (explode()) {
                // Keep exploding while possible
            }
            val split = split()
        } while (split)

        return this
    }

    var lastVisitedValueNode: Node? = null

    private fun Node.explode(depth: Int = 1): Boolean {
//        println("Visiting at depth $depth")
        if (depth == 5) {
//            println("Exploding node with children: ${left?.value}, ${right?.value}")
            // find the root
            var root = this
            while (root.parent != null) root = root.parent!!
            // push the elements to the sides
            lastVisitedValueNode = null
            root.addNextTo(this, left!!.value!!, true)
            lastVisitedValueNode = null
            root.addNextTo(this, right!!.value!!, false)
            // replace this with a 0
            if (parent!!.left == this) parent!!.left = Node(value = 0, parent = parent) else parent!!.right =
                Node(value = 0, parent = parent)
            return true
        }

        val leftExploded = if (left?.isValue == false) left!!.explode(depth + 1) else false
        val rightExploded = if (!leftExploded && right?.isValue == false) right!!.explode(depth + 1) else false
        return leftExploded || rightExploded
    }

    private fun Node.addNextTo(node: Node, value: Int, leftToRight: Boolean) {
        when {
            isValue -> lastVisitedValueNode = this
            this == node -> lastVisitedValueNode?.value = lastVisitedValueNode?.value!! + value
            leftToRight -> {
                left?.addNextTo(node, value, true)
                right?.addNextTo(node, value, true)
            }
            else -> {
                right?.addNextTo(node, value, false)
                left?.addNextTo(node, value, false)
            }
        }
    }

    private fun Node.split(): Boolean {
        val value = value
        if (value != null && value >= 10) {
//            println("Splitting node with value: $value")
            // replace this with a pair formed by the split
            val left = Node(value = floor(value / 2.0).toInt())
            val right = Node(value = ceil(value / 2.0).toInt())
            val split = Node(
                left = left,
                right = right,
                parent = parent
            )
            left.parent = split
            right.parent = split

            if (parent!!.left == this) {
                parent!!.left = split.also { it.parent = parent }
            } else {
                parent!!.right = split.also { it.parent = parent }
            }
            return true
        }

        val leftSplit = if (left != null) left!!.split() else false
        val rightSplit = if (!leftSplit && right != null) right!!.split() else false
        return leftSplit || rightSplit
    }

    private fun Node.magnitude(): Int {
        return when {
            isValue -> value!!
            else -> left!!.magnitude() * 3 + right!!.magnitude() * 2
        }
    }

    private fun readTree(raw: String): Node {
        val root = Node()
        var pointer = 1
        var currentNode = root
        while (pointer < raw.lastIndex) {
            val char = raw[pointer]
            when {
                char == '[' -> {
                    val emptyNode = Node(parent = currentNode)
                    currentNode.putChildren(emptyNode)
                    currentNode = emptyNode
                }
                char.isDigit() -> currentNode.putChildren(Node(char.digitToInt(), parent = currentNode))

                char == ']' -> currentNode = currentNode.parent!!
            }
            pointer++
        }
        return root
    }

    override fun part1(input: List<String>): Any {
        return input.indices.drop(1).fold(readTree(input.first())) { acc, i ->
            (acc + readTree(input[i])).apply { this.reduce() }
        }.magnitude()
    }

    override fun part2(input: List<String>): Any {
        return input.indices.maxOf { i ->
            input.indices.filter { it != i }.maxOf { j ->
                (readTree(input[i]) + readTree(input[j])).reduce().magnitude()
            }
        }
    }
}