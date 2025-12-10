#!/usr/bin/env kotlin

import java.util.ArrayDeque
import kotlin.math.max
import kotlin.math.min
import kotlin.time.TimeSource

data class Coord(val x: Long, val y: Long)

val start = TimeSource.Monotonic.markNow()

val reds = generateSequence { readlnOrNull() }
    .mapNotNull { line ->
        val trimmed = line.trim()
        if (trimmed.isEmpty()) return@mapNotNull null
        val parts = trimmed.split(",")
        if (parts.size != 2) return@mapNotNull null
        val (x, y) = parts
        Coord(x.toLong(), y.toLong())
    }
    .toList()

val answer: Long = if (reds.size < 2) {
    0L
} else {
    // Coordinate compression to avoid massive grids: use tile boundaries at every red x/y and +/- 1 margin.
    val xBreaks = mutableSetOf<Long>()
    val yBreaks = mutableSetOf<Long>()
    var minRx = Long.MAX_VALUE
    var maxRx = Long.MIN_VALUE
    var minRy = Long.MAX_VALUE
    var maxRy = Long.MIN_VALUE
    for (r in reds) {
        if (r.x < minRx) minRx = r.x
        if (r.x > maxRx) maxRx = r.x
        if (r.y < minRy) minRy = r.y
        if (r.y > maxRy) maxRy = r.y
        xBreaks.add(r.x)
        xBreaks.add(r.x + 1)
        yBreaks.add(r.y)
        yBreaks.add(r.y + 1)
    }
    // Margin to guarantee an outside cell.
    xBreaks.add(minRx - 1)
    xBreaks.add(maxRx + 2)
    yBreaks.add(minRy - 1)
    yBreaks.add(maxRy + 2)

    // Helper to register segment endpoints.
    fun addBreakFor(value: Long, axis: Char) {
        when (axis) {
            'x' -> {
                xBreaks.add(value)
                xBreaks.add(value + 1)
            }
            'y' -> {
                yBreaks.add(value)
                yBreaks.add(value + 1)
            }
        }
    }

    // Build boundary segments (reds are connected in order, wrapping).
    val segments = mutableListOf<Pair<Coord, Coord>>()
    for (i in reds.indices) {
        val a = reds[i]
        val b = reds[(i + 1) % reds.size]
        segments += a to b
        addBreakFor(a.x, 'x')
        addBreakFor(b.x, 'x')
        addBreakFor(a.y, 'y')
        addBreakFor(b.y, 'y')
    }

    val xs = xBreaks.toList().sorted()
    val ys = yBreaks.toList().sorted()
    val xIndex = xs.withIndex().associate { it.value to it.index }
    val yIndex = ys.withIndex().associate { it.value to it.index }
    val cellWidth = LongArray(xs.size - 1) { xs[it + 1] - xs[it] }
    val cellHeight = LongArray(ys.size - 1) { ys[it + 1] - ys[it] }

    val w = xs.size - 1
    val h = ys.size - 1
    val boundaryGrid = Array(h) { BooleanArray(w) }

    // Paint boundary cells (covers all tiles along each segment) without per-tile expansion.
    for ((a, b) in segments) {
        if (a.x == b.x) {
            val yMin = min(a.y, b.y)
            val yMax = max(a.y, b.y)
            val xi = xIndex.getValue(a.x)
            val xiCell = xi // cell whose left edge is at a.x
            val yStart = yIndex.getValue(yMin)
            val yEnd = yIndex.getValue(yMax + 1) - 1
            for (yy in yStart..yEnd) boundaryGrid[yy][xiCell] = true
        } else if (a.y == b.y) {
            val xMin = min(a.x, b.x)
            val xMax = max(a.x, b.x)
            val yi = yIndex.getValue(a.y)
            val yiCell = yi
            val xStart = xIndex.getValue(xMin)
            val xEnd = xIndex.getValue(xMax + 1) - 1
            for (xx in xStart..xEnd) boundaryGrid[yiCell][xx] = true
        } else {
            error("Non-axis-aligned segment between $a and $b")
        }
    }

    // Flood fill on compressed grid to find outside cells.
    val outside = Array(h) { BooleanArray(w) }
    val dq: ArrayDeque<Pair<Int, Int>> = ArrayDeque()
    dq.add(0 to 0) // corner in the padded margin
    outside[0][0] = true
    val dirs = arrayOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)
    while (dq.isNotEmpty()) {
        val (cx, cy) = dq.removeFirst()
        for ((dx, dy) in dirs) {
            val nx = cx + dx
            val ny = cy + dy
            if (nx !in 0 until w || ny !in 0 until h) continue
            if (outside[ny][nx]) continue
            if (boundaryGrid[ny][nx]) continue
            outside[ny][nx] = true
            dq.add(nx to ny)
        }
    }

    // Allowed tiles are boundary plus interior (not outside).
    val allowed = Array(h) { BooleanArray(w) }
    for (y in 0 until h) {
        for (x in 0 until w) {
            if (boundaryGrid[y][x]) {
                allowed[y][x] = true
            } else if (!outside[y][x]) {
                allowed[y][x] = true
            }
        }
    }

    // Prefix sums weighted by cell areas.
    val prefix = Array(h + 1) { LongArray(w + 1) }
    for (y in 0 until h) {
        var rowSum = 0L
        for (x in 0 until w) {
            if (allowed[y][x]) {
                rowSum += cellWidth[x] * cellHeight[y]
            }
            prefix[y + 1][x + 1] = prefix[y][x + 1] + rowSum
        }
    }

    fun rectAreaSum(x1: Int, y1: Int, x2: Int, y2: Int): Long {
        return prefix[y2 + 1][x2 + 1] - prefix[y1][x2 + 1] - prefix[y2 + 1][x1] + prefix[y1][x1]
    }

    // Evaluate all red-corner rectangles.
    var maxArea = 0L
    for (i in reds.indices) {
        val a = reds[i]
        for (j in i + 1 until reds.size) {
            val b = reds[j]
            val xMin = min(a.x, b.x)
            val xMax = max(a.x, b.x)
            val yMin = min(a.y, b.y)
            val yMax = max(a.y, b.y)

            val expectedArea = (xMax - xMin + 1) * (yMax - yMin + 1)

            val xi1 = xIndex.getValue(xMin)
            val xi2 = xIndex.getValue(xMax + 1) - 1
            val yi1 = yIndex.getValue(yMin)
            val yi2 = yIndex.getValue(yMax + 1) - 1

            val covered = rectAreaSum(xi1, yi1, xi2, yi2)
            if (covered == expectedArea && covered > maxArea) {
                maxArea = covered
            }
        }
    }

    maxArea
}

println(answer)
println("Ran in ${start.elapsedNow().inWholeMilliseconds}ms")
