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

fun List<String>.isInterConnected(): Boolean =
    all { a ->
        all { b -> b == a || connections[b]!!.contains(a) }
    }

fun List<String>.pairPermutations(computers: List<String>, startIndex: Int = 0): Sequence<List<String>> = sequence {
    var i = startIndex
    while (i < size) {
        val computer = get(i)
        val newComputers = ArrayList<String>(computers.size + 1)
        newComputers.addAll(computers)
        newComputers.add(computer)
        newComputers.sort()
        yield(newComputers)
        yieldAll(pairPermutations(newComputers, i + 1))
        ++i
    }
}

val biggestSet = connections.entries
    .asSequence()
    .flatMap { (a, connections)  -> connections.pairPermutations(listOf(a)) }
    .distinct()
    .filter { it.isInterConnected() }
    .maxBy { it.size }


println(biggestSet.joinToString(","))

println("Ran in ${startTime.elapsedNow()}")