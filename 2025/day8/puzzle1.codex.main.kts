#!/usr/bin/env kotlin

import java.io.File

data class Point(val x: Int, val y: Int, val z: Int)

private class DSU(size: Int) {
    private val parent = IntArray(size) { it }
    private val compSize = IntArray(size) { 1 }

    fun find(x: Int): Int {
        var v = x
        while (parent[v] != v) {
            parent[v] = parent[parent[v]]
            v = parent[v]
        }
        return v
    }

    fun union(a: Int, b: Int): Boolean {
        var ra = find(a)
        var rb = find(b)
        if (ra == rb) return false
        if (compSize[ra] < compSize[rb]) ra = rb.also { rb = ra }
        parent[rb] = ra
        compSize[ra] += compSize[rb]
        return true
    }

    fun componentSizes(): List<Int> {
        val seen = HashSet<Int>()
        val sizes = mutableListOf<Int>()
        for (i in parent.indices) {
            val r = find(i)
            if (seen.add(r)) sizes += compSize[r]
        }
        return sizes
    }
}

private data class PairDist(val i: Int, val j: Int, val distSq: Long)

private fun parsePoints(path: String): List<Point> =
    File(path).readLines().filter { it.isNotBlank() }.map { line ->
        val (x, y, z) = line.split(",").map { it.trim().toInt() }
        Point(x, y, z)
    }

private fun squaredDistance(a: Point, b: Point): Long {
    val dx = (a.x - b.x).toLong()
    val dy = (a.y - b.y).toLong()
    val dz = (a.z - b.z).toLong()
    return dx * dx + dy * dy + dz * dz
}

private fun productOfThreeLargestAfterConnections(points: List<Point>, connections: Int): Long {
    val n = points.size
    val dsu = DSU(n)

    val pairs = ArrayList<PairDist>(n * (n - 1) / 2)
    for (i in 0 until n - 1) {
        for (j in i + 1 until n) {
            pairs += PairDist(i, j, squaredDistance(points[i], points[j]))
        }
    }

    val ordered = pairs.sortedWith(
        compareBy<PairDist> { it.distSq }
            .thenBy { it.i }
            .thenBy { it.j }
    )

    val steps = connections.coerceAtMost(ordered.size)
    for (k in 0 until steps) {
        val p = ordered[k]
        dsu.union(p.i, p.j)
    }

    val largest = dsu.componentSizes().sortedDescending().take(3)
    return largest.fold(1L) { acc, size -> acc * size }
}

fun main() {
    val points = parsePoints("input.txt")
    val answer = productOfThreeLargestAfterConnections(points, 1000)
    println(answer)
}

main()
