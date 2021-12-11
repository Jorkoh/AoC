package utils

class Grid<T>(private val values: Array<Array<T>>) {

    companion object {
        inline fun <reified T> List<List<T>>.toGrid() = Grid(map(List<T>::toTypedArray).toTypedArray())
        fun List<String>.toIntGrid() = Grid(
            map { line -> line.map(Char::digitToInt).toTypedArray() }.toTypedArray()
        )

    }

    private val width = values.first().size
    private val height = values.size

    private val rowIndices = values.first().indices
    private val colIndices = values.indices

    operator fun get(coords: Pair<Int, Int>) = values[coords.second][coords.first]
    operator fun set(coords: Pair<Int, Int>, value: T) {
        values[coords.second][coords.first] = value
    }

    fun forEach(action: (T) -> Unit): Unit {
        for (x in rowIndices) {
            for (y in colIndices) {
                action(values[y][x])
            }
        }
    }

    fun forEachIndex(action: (Pair<Int, Int>) -> Unit): Unit {
        for (x in rowIndices) {
            for (y in colIndices) {
                action(x to y)
            }
        }
    }

    fun transformEach(action: (T) -> T): Unit {
        for (x in rowIndices) {
            for (y in colIndices) {
                values[y][x] = action(values[y][x])
            }
        }
    }

    fun all(predicate: (T) -> Boolean) = values.all { row -> row.all { predicate(it) } }

    fun neighbors(coords: Pair<Int, Int>): List<T> {
        return buildList {
            for (x in coords.first - 1..coords.first + 1) {
                for (y in coords.second - 1..coords.second + 1) {
                    if (x in rowIndices && y in colIndices) add(values[y][x])
                }
            }
        }
    }

    fun neighborIndices(coords: Pair<Int, Int>): List<Pair<Int, Int>> {
        return buildList {
            for (x in coords.first - 1..coords.first + 1) {
                for (y in coords.second - 1..coords.second + 1) {
                    if (x in rowIndices && y in colIndices) add(x to y)
                }
            }
        }
    }

    fun print() {
        for (y in colIndices) {
            println(values[y].joinToString(" "))
        }
        println()
    }
}