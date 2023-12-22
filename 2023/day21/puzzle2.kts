#!/usr/bin/env kotlin

import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.roundToLong
import kotlin.math.sqrt
import kotlin.time.TimeSource
import kotlin.time.toDuration

val start = TimeSource.Monotonic.markNow()
val debug = false

val requiredSteps = args.getOrNull(0)?.toLong() ?: 26501365L

val grid = generateSequence(::readlnOrNull)
        .toList()
val h = grid.size
val w = grid[0].length

val (startX, startY) = grid
        .asSequence()
        .withIndex()
        .mapNotNull { (y, row) ->
            val x = row.indexOf('S')
            if (x >= 0) x to y else null
        }
        .first()

data class Step(val x: Int, val y: Int, val steps: Long = 0L)

fun possiblePlots(startX: Int, startY: Int, requiredSteps: Long): Long {
    val seen = mutableSetOf(startX to startY)
    val queue = ArrayDeque(listOf(Step(startX, startY)))
    var count = 0L
    while (queue.isNotEmpty()) {
        val step = queue.removeFirst()
        if ((requiredSteps - step.steps) % 2 == 0L) {
            ++count
        }
        if (step.steps == requiredSteps) continue
        for (dx in -1..1) {
            for (dy in -1..1) {
                if ((dx.absoluteValue == 1) == (dy.absoluteValue == 1)) continue
                val x = step.x + dx
                val y = step.y + dy
                if (grid[((y % h) + h) % h][((x % w) + w) % w] == '#') continue
                if (!seen.add(x to y)) continue
                queue.addLast(Step(x, y, step.steps + 1))
            }
        }
    }
    return count
}

val points = (0 until 3).map { w / 2 + w * it.toLong() }.map { x -> x to possiblePlots(startX, startY, x) }

val c = points[0].second
val f2 = points[1].second - c
val f3 = points[2].second - c

val a = (f2 * -2.0 + f3) / 2.0
val b = f2 - a

val x = (requiredSteps - w / 2) / w.toDouble()
val y = (a * (x * x) + b * x + c).roundToLong()
println(y)

println(start.elapsedNow())