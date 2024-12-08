#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val debug = true
fun debug(msg: String) {
    if (debug) {
        println(msg)
    }
}

data class Coordinate(val x: Int, val y: Int)

val start = TimeSource.Monotonic.markNow()
val antennas = mutableMapOf<Char, MutableList<Coordinate>>()
val map = generateSequence { readlnOrNull() }
    .onEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c != '.') {
                antennas.getOrPut(c, ::mutableListOf).add(Coordinate(x, y))
            }
        }
    }
    .toList()

val w = map[0].length
val h = map.size

fun computeAntiNodes(x: Int, y: Int, dx: Int, dy: Int): Sequence<Coordinate> = sequence {
    var mx = x
    var my = y
    while (mx in 0 until w && my in 0 until h) {
        yield(Coordinate(mx, my))
        mx += dx
        my += dy
    }
}

val coordinates = antennas.values.asSequence()
    .flatMap { antennas ->
        sequence {

            antennas.forEachIndexed { i, a1 ->
                antennas.subList(i + 1, antennas.size).forEach { a2 ->
                    val dx = a2.x - a1.x
                    val dy = a2.y - a1.y
                    yieldAll(computeAntiNodes(a1.x, a1.y, dx, dy))
                    yieldAll(computeAntiNodes(a2.x, a2.y, -dx, -dy))
                }
            }
        }
    }
    .distinct()
    .count()

println("Found $coordinates anti-nodes")
println("Ran in ${start.elapsedNow()}")