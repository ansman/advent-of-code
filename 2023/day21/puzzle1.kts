#!/usr/bin/env kotlin

import java.util.*
import kotlin.math.absoluteValue
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

val grid = generateSequence(::readlnOrNull)
        .toList()

val (startX, startY) = grid
        .asSequence()
        .withIndex()
        .mapNotNull { (y, row) ->
            val x = row.indexOf('S')
            if (x >= 0) x to y else null
        }
        .first()

data class Step(val x: Int, val y: Int, val steps: Int = 0)

val seen = mutableSetOf<Step>()
val queue = ArrayDeque(listOf(Step(startX, startY)))
val plots = mutableSetOf<Pair<Int, Int>>()
while (queue.isNotEmpty()) {
    val step = queue.removeFirst()
    if (step.steps == 64) {
        plots.add(step.x to step.y)
        continue
    }
    for (dx in -1..1) {
        for (dy in -1..1) {
            if ((dx.absoluteValue == 1) == (dy.absoluteValue == 1)) continue
            val x = step.x + dx
            val y = step.y + dy
            if (x < 0 || y < 0 || x >= grid[0].length || y >= grid.size) continue
            if (grid[y][x] == '#') continue
            val next = Step(x, y, step.steps + 1)
            if (seen.add(next)) {
                queue.addLast(next)
            }
        }
    }
}

println(plots.size)
println(start.elapsedNow())