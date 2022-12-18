#!/usr/bin/env kotlin

import kotlin.math.max
import kotlin.math.min

data class Cube(val x: Long, val y: Long, val z: Long)

val lookup = mutableMapOf<Long, MutableMap<Long, MutableMap<Long, Cube>>>()
val cubes = buildList<Cube> {
    while (true) {
        val line = readLine() ?: break
        val (x, y, z) = line.split(",").map { it.toLong() }
        val cube = Cube(x, y, z)
        add(cube)
        lookup.getOrPut(cube.x, ::mutableMapOf).getOrPut(cube.y, ::mutableMapOf).put(cube.z, cube)
    }
}

val seen = mutableSetOf<Cube>()
var surfaceArea = 0L
for (cube in cubes) {
    if (cube in seen) {
        continue
    }

    val queue = ArrayDeque<Cube>()
    queue.add(cube)

    fun Cube.enqueueAdjacent(dx: Long = 0, dy: Long = 0, dz: Long = 0): Long {
        val adjacent = lookup[x + dx]?.get(y + dy)?.get(z + dz) ?: return 0L
        if (adjacent !in seen) {
            queue.add(adjacent)
        }
        return 1L
    }

    while (queue.isNotEmpty()) {
        val c = queue.removeFirst()
        if (!seen.add(c)) {
            continue
        }

        surfaceArea += 6L -
                c.enqueueAdjacent(dx = -1) -
                c.enqueueAdjacent(dx = 1) -
                c.enqueueAdjacent(dy = -1) -
                c.enqueueAdjacent(dy = 1) -
                c.enqueueAdjacent(dz = -1) -
                c.enqueueAdjacent(dz = 1)
    }
}
println("Total surface area is $surfaceArea")