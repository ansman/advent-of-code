#!/usr/bin/env kotlin

// Repeatedly remove any roll ('@') with fewer than four adjacent rolls in the
// 8-neighborhood, updating accessibility after each removal. The total number
// of removed rolls is the answer.

val grid = generateSequence { readlnOrNull() }
    .map { it.trim() }
    .filter { it.isNotEmpty() }
    .toList()

require(grid.isNotEmpty()) { "Input grid is empty" }
val height = grid.size
val width = grid.first().length
require(grid.all { it.length == width }) { "All rows must be the same length" }

val directions = listOf(
    -1 to -1, 0 to -1, 1 to -1,
    -1 to 0, /*self*/ 1 to 0,
    -1 to 1, 0 to 1, 1 to 1,
)

val isRoll = Array(height) { y ->
    BooleanArray(width) { x -> grid[y][x] == '@' }
}
val neighborCount = Array(height) { IntArray(width) }

for (y in 0 until height) {
    for (x in 0 until width) {
        if (!isRoll[y][x]) continue
        var count = 0
        for ((dx, dy) in directions) {
            val nx = x + dx
            val ny = y + dy
            if (nx !in 0 until width || ny !in 0 until height) continue
            if (isRoll[ny][nx]) count++
        }
        neighborCount[y][x] = count
    }
}

val queue: ArrayDeque<Pair<Int, Int>> = ArrayDeque()
for (y in 0 until height) {
    for (x in 0 until width) {
        if (isRoll[y][x] && neighborCount[y][x] < 4) queue.add(x to y)
    }
}

var removed = 0
while (queue.isNotEmpty()) {
    val (x, y) = queue.removeFirst()
    if (!isRoll[y][x]) continue // might have been removed already

    isRoll[y][x] = false
    removed++

    for ((dx, dy) in directions) {
        val nx = x + dx
        val ny = y + dy
        if (nx !in 0 until width || ny !in 0 until height) continue
        if (!isRoll[ny][nx]) continue

        neighborCount[ny][nx]--
        if (neighborCount[ny][nx] == 3) {
            queue.add(nx to ny)
        }
    }
}

println(removed)
