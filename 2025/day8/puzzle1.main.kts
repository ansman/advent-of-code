#!/usr/bin/env kotlin

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

val pairCount = args.getOrNull(0)?.toInt() ?: 1000

data class Box(val x: Int, val y: Int, val z: Int) {
    override fun toString(): String = "%03d,%03d,%03d".format(Locale.ROOT, x, y, z)
}

val distances = mutableMapOf<Box, List<Pair<Box, Double>>>()
val boxes = generateSequence { readlnOrNull() }
    .map { it.split(',') }
    .map { it.map(String::toInt) }
    .map { (x, y, z) -> Box(x, y, z) }
    .toList()

data class Connection(val box1: Box, val box2: Box) {
    val distance: Double = sqrt(
        ((box1.x - box2.x).toDouble()).pow(2) +
                ((box1.y - box2.y).toDouble()).pow(2) +
                ((box1.z - box2.z).toDouble()).pow(2)
    )

    override fun toString(): String = "$box1-$box2"
}

class Connections {
    private val connections = mutableMapOf<Box, MutableSet<Box>>()

    fun add(connection: Connection) = apply {
        println("Connecting $connection")

        val boxes = connections.getOrPut(connection.box1, ::mutableSetOf) +
                connections.getOrPut(connection.box2, ::mutableSetOf) +
                setOf(connection.box1, connection.box2)

        for (b in boxes) {
            connections.getOrPut(b, ::mutableSetOf).addAll(boxes)
        }
    }

    fun computeLargest(): Int =
        connections.values
        .distinct()
        .toList()
        .sortedByDescending { it.size }
        .take(3)
        .map { it.size }.reduce { acc, size -> acc * size }
}

val connections = boxes.withIndex()
    .asSequence()
    .flatMap { (i, box1) ->
        boxes.subList(i + 1, boxes.size).map { box2 -> Connection(box1, box2) }
    }
    .sortedBy { it.distance }
    .take(pairCount)
    .fold(Connections(), Connections::add)
    .computeLargest()
    .also { println(it) }

println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")