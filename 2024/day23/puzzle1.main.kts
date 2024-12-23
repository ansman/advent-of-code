#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

class Computers(var a: String, var b: String, var c: String) {
    init {
        sort()
    }
    fun hasNodeStartingWith(l: Char): Boolean = a.startsWith(l) || b.startsWith(l) || c.startsWith(l)

    fun sort() {
        require(a != b && a != c && b != c)
        val a = a
        val b = b
        val c = c
        this.a = minOf(a, minOf(b, c))
        this.c = maxOf(a, maxOf(b, c))
        this.b = a.takeUnless { it == this.a || it == this.c }
            ?: c.takeUnless { it == this.a || it == this.c }
            ?: b
    }

    override fun equals(other: Any?): Boolean =
        this === other || other is Computers && a == other.a && b == other.b && c == other.c

    override fun hashCode(): Int {
        var result = a.hashCode()
        result = 31 * result + b.hashCode()
        result = 31 * result + c.hashCode()
        return result
    }

    override fun toString(): String = "$a,$b,$c"
}

val connections = mutableMapOf<String, MutableList<String>>()

fun removeNode(node: String) {
    connections.remove(node)!!.forEach {
        connections[it]!!.remove(node)
    }
}

generateSequence { readlnOrNull() }
    .map { it.split('-') }
    .forEach { (a, b) ->
        connections.getOrPut(a, ::mutableListOf).add(b)
        connections.getOrPut(b, ::mutableListOf).add(a)
    }

fun List<String>.pairPermutations(a: String): Sequence<Computers> = sequence {
    forEachIndexed { i, b ->
        if (b == a) {
            return@forEachIndexed
        }
        var j = i + 1
        while (j < size) {
            val c = get(j)
            if (c != a && c in connections[b]!! && b in connections[c]!!) {
                yield(Computers(a, b, c))
            }
            ++j
        }
    }
}

fun generateComputers(a: String) = sequence<Computers> {
    val aConnections = connections.getValue(a)
    yieldAll(aConnections.pairPermutations(a))
}

val allComputers = sequence {
    while (connections.isNotEmpty()) {
        val node = connections.keys.first()
        yieldAll(generateComputers(node))
        removeNode(node)
    }
}

val count = allComputers
    .distinct()
    .onEach { debug(it) }
    .count { it.hasNodeStartingWith('t') }

println("$count cliques has a node starting with T")

println("Ran in ${startTime.elapsedNow()}")