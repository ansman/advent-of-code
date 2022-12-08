#!/usr/bin/env kotlin

import kotlin.math.max

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

fun score(
        startX: Int,
        startY: Int,
        dx: Int,
        dy: Int
): Long {
    var x = startX
    var y = startY
    val myTree = grid[y][x]
    var score = 0L
    x += dx
    y += dy
    while (x >= 0 && x <= width - 1 && y >= 0 && y <= height - 1) {
        ++score
        val height = grid[y][x]
        if (height >= myTree) {
            break
        }
        x += dx
        y += dy
    }
    return score
}

var bestScore = 0L
repeat(width) { x ->
    repeat(height) { y ->
        bestScore = max(
                bestScore,
                score(x, y, 1, 0) *
                        score(x, y, -1, 0) *
                        score(x, y, 0, 1) *
                        score(x, y, 0, -1)
        )
    }
}
println(bestScore)