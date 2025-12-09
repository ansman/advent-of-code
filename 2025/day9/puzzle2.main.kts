#!/usr/bin/env kotlin

import kotlin.math.abs
import kotlin.time.TimeSource


val startTime = TimeSource.Monotonic.markNow()

data class Coord(val x: Long, val y: Long)
data class Edge(val from: Coord, val to: Coord) {
    val isHorizontal: Boolean = from.y == to.y
    val isVertical: Boolean = from.x == to.x
    fun normalize(): Edge {
        return if (isHorizontal) {
            if (from.x < to.x) {
                Edge(from, to)
            } else {
                Edge(to, from)
            }
        } else {
            if (from.y < to.y) {
                Edge(from, to)
            } else {
                Edge(to, from)
            }
        }
    }
}
data class Run(val yStart: Long, val yEnd: Long, val intervals: List<LongRange>)

val redTiles = generateSequence { readlnOrNull() }
    .map { it.split(",") }
    .map { (x, y) -> Coord(x.toLong(), y.toLong()) }
    .toList()

val edges = List(redTiles.size) { index ->
    val from = redTiles[index]
    val to = redTiles[(index + 1) % redTiles.size]
    Edge(from, to).normalize()
}

val (horizontalEdges, verticalEdges) = edges.partition { it.isHorizontal }

fun computeSpansAt(y: Long): List<LongRange> {
    val scanline = y + 0.5
    val spans = verticalEdges
        .filter { scanline >= it.from.y && scanline < it.to.y }
        .map { it.from.x }
        .sorted()
        .windowed(2, 2) { (l, r) -> l..r }
        .map { it.first..it.last }
        .plus(
            horizontalEdges
                .asSequence()
                .filter { edge -> edge.from.y == y }
                .map { it.from.x..it.to.x }
                .toList()
        )
        .sortedBy { it.first }

    if (spans.isEmpty()) return emptyList()
    val merged = mutableListOf<LongRange>()
    for (span in spans) {
        if (merged.isEmpty()) {
            merged.add(span)
            continue
        }
        val last = merged.last()
        if (span.first <= last.last + 1) { // touching/overlap (inclusive tiles)
            merged[merged.size - 1] = last.first..maxOf(last.last, span.last)
        } else merged.add(span)
    }
    return merged
}

val minY = edges.minOf { it.from.y }
val maxY = edges.maxOf { it.to.y }
var currentSpans = computeSpansAt(minY)
val runs = mutableListOf<Run>()
var runStart = minY
for (y in minY+1 until maxY) {
    val next = y + 1
    val nextSpans = computeSpansAt(next)
    if (nextSpans == currentSpans) {
        continue
    }
    runs += Run(runStart, y, currentSpans)
    runStart = next
    currentSpans = nextSpans
}
runs += Run(runStart, maxY, currentSpans)

fun rowCovers(intervals: List<LongRange>, span: LongRange): Boolean =
    intervals.any { it.first <= span.first && it.last >= span.last }

fun rectangleInside(xSpan: LongRange, ySpan: LongRange): Boolean {
    for (run in runs) {
        if (run.yEnd < ySpan.first) continue
        if (run.yStart > ySpan.last) break
        if (!rowCovers(run.intervals, xSpan)) return false
    }
    return true
}

var best = 0L
repeat(redTiles.size) { i ->
    val a = redTiles[i]
    for (j in i + 1 until redTiles.size) {
        val b = redTiles[j]
        val xSpan = minOf(a.x, b.x)..maxOf(a.x, b.x)
        val ySpan = minOf(a.y, b.y)..maxOf(a.y, b.y)
        if (!rectangleInside(xSpan, ySpan)) continue
        val area = (abs(a.x - b.x) + 1) * (abs(a.y - b.y) + 1)
        if (area > best) best = area
    }
}

println(best)


println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")