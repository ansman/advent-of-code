#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.math.sign

data class Position(val x: Int = 0, val y: Int = 0)

fun Position.movedBy(dx: Int = 0, dy: Int = 0): Position = copy(x = x + dx, y = y + dy)

val visited = mutableSetOf<Position>()
var knots = Array(10) { Position(0, 0) }
visited += knots.last()

fun moveBy(index: Int, offsetX: Int = 0, offsetY: Int = 0) {
    val head = knots[index].movedBy(offsetX, offsetY)
    knots[index] = head
    val tail = knots.getOrNull(index + 1) ?: return

    val dx = head.x - tail.x
    val dy = head.y - tail.y

    if (dx.absoluteValue <= 1 && dy.absoluteValue <= 1) {
        return
    }
    if (dx == 0) {
        moveBy(index + 1, offsetY = offsetY)
    } else if (dy == 0) {
        moveBy(index + 1, offsetX = offsetX)
    } else {
        // Diagonal move
        moveBy(
                index + 1,
                offsetX = (dx.absoluteValue - 1).coerceAtLeast(1) * dx.sign,
                offsetY = (dy.absoluteValue - 1).coerceAtLeast(1) * dy.sign
        )
    }
}

while (true) {
    val line = readLine() ?: break
    val (dir, steps) = line.split(" ")
    val offset = when (dir) {
        "L" -> Position(x = -1)
        "U" -> Position(y = -1)
        "R" -> Position(x = 1)
        "D" -> Position(y = 1)
        else -> error("Unknown direction $dir")
    }
    repeat(steps.toInt()) {
        moveBy(0, offset.x, offset.y)
        visited += knots.last()
    }
}

println(visited.size)