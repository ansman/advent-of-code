#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = args.isNotEmpty()

class Graph {
    val vertexes = mutableListOf<Pair<String, String>>()
    val nodes = mutableSetOf<String>()

    fun add(line: String) {
        val node = line.substringBefore(": ")
        val connections = line.substringAfter(": ").split(" ")
        for (connection in connections) {
            add(node, connection)
        }
    }

    fun add(from: String, to: String) {
        vertexes.add(from to to)
        vertexes.add(to to from)
        nodes.add(from)
        nodes.add(to)
    }

    fun copy(): Graph = Graph().also { g ->
        g.vertexes.addAll(vertexes)
        g.nodes.addAll(nodes)
    }

    fun computeMinimumCuts(): Pair<Int, List<List<String>>> {
        while (nodes.size > 2) {
            mergeNodes()
        }
        return vertexes.size / 2 to nodes.map { it.split(" ") }
    }

    private fun mergeNodes() {
        val merged = vertexes.random()
        val new = "${merged.first} ${merged.second}"
        if (debug) println("Cutting ${merged.first} <> ${merged.second}")

        nodes.remove(merged.first)
        nodes.remove(merged.second)
        nodes.add(new)
        val it = vertexes.listIterator()
        while (it.hasNext()) {
            val (from, to) = it.next()
            when {
                from == merged.first && to == merged.second -> it.remove()
                to == merged.first && from == merged.second -> it.remove()
                from == merged.first -> it.set(new to to)
                from == merged.second -> it.set(new to to)
                to == merged.first -> it.set(from to new)
                to == merged.second -> it.set(from to new)
            }
        }
    }
}

val graph = Graph()
generateSequence(::readlnOrNull).forEach(graph::add)

while (true) {
    val g = graph.copy()
    val (cuts, groups) = g.computeMinimumCuts()
    if (cuts != 3) {
        continue
    }
    if (debug) println("Group count: ${groups.size}")
    if (debug) groups.forEach(::println)
    if (groups.size == 2) {
        println(groups[0].size * groups[1].size)
        break
    }
    if (debug) println()
}

println(startTime.elapsedNow())