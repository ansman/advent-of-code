#!/usr/bin/env kotlin

import kotlin.math.sign

enum class Cell {
    Rock,
    Sand,
    Air,
}

enum class SandMovement {
    Down,
    DiagonalLeft,
    DiagonalRight;

    fun transformX(x: Int): Int = when (this) {
        Down -> x
        DiagonalLeft -> x - 1
        DiagonalRight -> x + 1
    }

    fun transformY(y: Int): Int = y + 1
}

val grid = mutableMapOf<Int, MutableMap<Int, Cell>>()

fun cellAt(x: Int, y: Int): Cell = grid[y]?.get(x) ?: Cell.Air
fun setCellAt(x: Int, y: Int, cell: Cell) {
    grid.getOrPut(y, ::mutableMapOf).put(x, cell)
}

fun isCellFree(x: Int, y: Int) = cellAt(x, y) == Cell.Air

fun drawRock(startX: Int, startY: Int, endX: Int, endY: Int) {
    var x = startX
    var y = startY
    val dx = (endX - startX).sign
    val dy = (endY - startY).sign
    while (true) {
        setCellAt(x, y, Cell.Rock)
        if (x == endX && y == endY) {
            break
        } else {
            x += dx
            y += dy
        }
    }
}

while (true) {
    val line = readLine() ?: break
    line.split(" -> ")
            .map { it.split(",").map { it.toInt() } }
            .zipWithNext()
            .forEach { (start, end) ->
                val (startX, startY) = start
                val (endX, endY) = end
                drawRock(startX, startY, endX, endY)
            }
}

val minX = grid.values.minOf {it.keys.min() }
val maxX = grid.values.maxOf {it.keys.max() }
val lowestPoint = grid.keys.max()

val spawnX = 500
val spawnY = 0
var sandCount = 0

fun drawDebugGrid(sandX: Int, sandY: Int) {
    for (y in 0..lowestPoint) {
        for (x in minX..maxX) {
            print(when {
                x == sandX && y == sandY -> 'o'
                x == spawnX && y == spawnY -> '+'
                else -> when (cellAt(x, y)) {
                    Cell.Rock -> '#'
                    Cell.Sand -> 'o'
                    Cell.Air -> ' '
                }
            })
        }
        println()
    }
    println()
}

outer@while (true) {
    var sandX = spawnX
    var sandY = spawnY
    inner@while (true) {
        if (sandY >= lowestPoint) {
            break@outer
        }
        for (movement in SandMovement.values()) {
            val newX = movement.transformX(sandX)
            val newY = movement.transformY(sandY)
            if (isCellFree(newX, newY)) {
                sandX = newX
                sandY = newY
                continue@inner
            }
        }
        // Sand cannot move
        setCellAt(sandX, sandY, Cell.Sand)
        ++sandCount
        break
    }
}
println(sandCount)