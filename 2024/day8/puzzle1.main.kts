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

val coordinates = antennas.values.asSequence()
    .flatMap { antennas ->
        sequence {
            antennas.forEachIndexed { i, a1 ->
                antennas.subList(i + 1, antennas.size).forEach { a2 ->
                    val dx = a2.x - a1.x
                    val dy = a2.y - a1.y
                    yield(Coordinate(a1.x - dx, a1.y - dy))
                    yield(Coordinate(a2.x + dx, a2.y + dy))
                }
            }
        }
    }
    .filter { it.x in 0 until w && it.y in 0 until h }
    .distinct()
    .count()

println("Found $coordinates anti-nodes")
println("Ran in ${start.elapsedNow()}")