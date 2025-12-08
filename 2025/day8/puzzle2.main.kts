#!/usr/bin/env kotlin

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

data class Box(val x: Long, val y: Long, val z: Long) {
    override fun toString(): String = "%03d,%03d,%03d".format(Locale.ROOT, x, y, z)
}

val distances = mutableMapOf<Box, List<Pair<Box, Double>>>()
val boxes = generateSequence { readlnOrNull() }
    .map { it.split(',') }
    .map { it.map(String::toLong) }
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

private fun <E> MutableCollection<E>.removeFirst(predicate: (E) -> Boolean): E? {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val element = iterator.next()
        if (predicate(element)) {
            iterator.remove()
            return element
        }
    }
    return null
}

class Connections {
    private val connections = mutableListOf<Set<Box>>()

    fun add(connection: Connection): Boolean {
        val cns = connections.removeFirst { connection.box1 in it }.orEmpty() +
                connections.removeFirst { connection.box2 in it }.orEmpty() +
                setOf(connection.box1, connection.box2)
        connections.add(cns)
        return cns.size == boxes.size
    }
}

val connections = Connections()
boxes.withIndex()
    .asSequence()
    .flatMap { (i, box1) ->
        boxes.subList(i + 1, boxes.size).map { box2 -> Connection(box1, box2) }
    }
    .sortedBy { it.distance }
    .first { connections.add(it) }
    .also { println(it) }
    .let { it.box1.x * it.box2.x }
    .also { println(it) }

println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")