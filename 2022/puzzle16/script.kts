#!/usr/bin/env kotlin

val grid = mutableListOf<List<Int>>()
while (true) {
    val line = readLine() ?: break
    grid.add(line.toCharArray().map { it.digitToInt() })
}

val width = grid[0].size
val height = grid.size

var visibleTrees = Array(height) { y ->
    BooleanArray(width) { x -> x == 0 || x == width - 1 || y == 0 || y == height - 1 }
}

fun scanVisibleTrees(
        startX: Int,
        startY: Int,
        dx: Int,
        dy: Int
) {
    var x = startX
    var y = startY
    var tallestSoFar = grid[y - dy][x - dx]
    while (x > 0 && x < width - 1 && y > 0 && y < height - 1) {
        var height = grid[y][x]
        if (height > tallestSoFar) {
            visibleTrees[y][x] = true
            tallestSoFar = height
        }
        x += dx
        y += dy
    }
}
repeat(width - 2) { i ->
    // Top to bottom
    scanVisibleTrees(i + 1, 1, 0, 1)
    // Bottom to top
    scanVisibleTrees(i + 1, height - 2, 0, -1)
}
repeat(height - 2) { i ->
    // Left to right
    scanVisibleTrees(1, i + 1, 1, 0)
    // Right to left
    scanVisibleTrees(width - 2, i + 1, -1, 0)
}
for (row in visibleTrees) {
    for (cell in row) {
        print(if (cell) "x" else " ")
    }
    println()
}
println(visibleTrees.sumOf { it.count { it } })