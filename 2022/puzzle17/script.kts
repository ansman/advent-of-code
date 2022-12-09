#!/usr/bin/env kotlin

import kotlin.math.absoluteValue
import kotlin.math.sign

data class Position(val x: Int = 0, val y: Int = 0)
fun Position.movedBy(dx: Int = 0, dy: Int = 0): Position = copy(x = x + dx, y = y + dy)

val visited = mutableSetOf<Position>()
var head = Position(0, 0)
var tail = head
visited += tail

fun moveBy(offset: Position) {
    head = head.movedBy(offset.x, offset.y)
    val dx = head.x - tail.x
    val dy = head.y - tail.y
    if (dx.absoluteValue <= 1 && dy.absoluteValue <= 1) {
        return
    }
    if (dx == 0) {
        tail = tail.movedBy(dy = offset.y)
    } else if (dy == 0) {
        tail = tail.movedBy(dx = offset.x)
    } else {
        // Diagonal move
        tail = tail.movedBy(
            dx = (dx.absoluteValue - 1).coerceAtLeast(1) * dx.sign,
            dy = (dy.absoluteValue - 1).coerceAtLeast(1) * dy.sign
        )
    }
    visited += tail
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
        moveBy(offset)
    }
}

println(visited.size)