#!/usr/bin/env kotlin

import kotlin.math.max
import kotlin.math.min
import kotlin.script.dependencies.ScriptContents
import kotlin.time.Duration.Companion.nanoseconds

val start = System.nanoTime()
Runtime.getRuntime().addShutdownHook(Thread {
    println("Total runtime ${(System.nanoTime() - start).nanoseconds}")
})

data class Coordinate(val x: Long, val y: Long, val z: Long)

val lookup = mutableMapOf<Long, MutableMap<Long, MutableMap<Long, Coordinate>>>()

var minX = Long.MAX_VALUE
var maxX = Long.MIN_VALUE
var minY = Long.MAX_VALUE
var maxY = Long.MIN_VALUE
var minZ = Long.MAX_VALUE
var maxZ = Long.MIN_VALUE

fun addCubeToLookup(cube: Coordinate) {
    lookup.getOrPut(cube.x, ::mutableMapOf).getOrPut(cube.y, ::mutableMapOf).put(cube.z, cube)
}

val cubes = buildList<Coordinate> {
    while (true) {
        val line = readLine() ?: break
        val (x, y, z) = line.split(",").map { it.toLong() }
        val cube = Coordinate(x, y, z)
        add(cube)
        addCubeToLookup(cube)
        minX = min(minX, cube.x)
        maxX = max(maxX, cube.x)
        minY = min(minY, cube.y)
        maxY = max(maxY, cube.y)
        minZ = min(minZ, cube.z)
        maxZ = max(maxZ, cube.z)
    }
}

fun getCube(x: Long, y: Long, z: Long): Coordinate? = lookup[x]?.get(y)?.get(z)
fun hasCube(x: Long, y: Long, z: Long) = getCube(x, y, z) != null
val Coordinate.isOutOfBounds: Boolean get() = x < minX || x > maxX || y < minY || y > maxY || z < minZ || z > maxZ

val adjacents = arrayOf(
        arrayOf(-1,  0,  0),
        arrayOf( 1,  0,  0),
        arrayOf( 0, -1,  0),
        arrayOf( 0,  1,  0),
        arrayOf( 0,  0, -1),
        arrayOf( 0,  0,  1),
)

inline fun runBfs(
        initial: Coordinate,
        getCoordinate: (x: Long, y: Long, z: Long) -> Coordinate?,
        onVisit: (Coordinate) -> Unit = {},
        onAdjacantVisit: (adjacent: Coordinate, isSeen: Boolean) -> Unit = { _, _ -> Unit },
        seen: MutableSet<Coordinate> = mutableSetOf(),
) {
    if (initial in seen) {
        return
    }

    val queue = ArrayDeque<Coordinate>()
    queue.add(initial)

    while (queue.isNotEmpty()) {
        val c = queue.removeFirst()
        if (!seen.add(c)) {
            continue
        }
        onVisit(c)
        for ((dx, dy, dz) in adjacents) {
            val adjacent = getCoordinate(c.x + dx, c.y + dy, c.z + dz) ?: continue
            val isSeen = adjacent in seen
            if (!isSeen) {
                queue.add(adjacent)
            }
            onAdjacantVisit(adjacent, isSeen)
        }
    }
}

fun fillInPotentialCavity(x: Long, y: Long, z: Long) {
    val seen = mutableSetOf<Coordinate>()
    runBfs(
            initial = Coordinate(x, y, z),
            getCoordinate = { x, y, z -> if (hasCube(x, y, z)) null else Coordinate(x, y, z) },
            onVisit = { coordinate ->
                if (coordinate.isOutOfBounds) {
                    return
                }
            },
            seen = seen
    )
    for (coordinate in seen) {
        addCubeToLookup(coordinate)
    }
}

for (x in minX..maxX) {
    for (y in minY..maxY) {
        for (z in minZ..maxZ) {
            if (!hasCube(x, y, z)) {
                fillInPotentialCavity(x, y, z)
            }
        }
    }
}


val seen = mutableSetOf<Coordinate>()
var surfaceArea = 0L
for (cube in cubes) {
    runBfs(
            initial = cube,
            getCoordinate = ::getCube,
            onVisit = { surfaceArea += 6 },
            onAdjacantVisit = { _, _ -> --surfaceArea },
            seen = seen
    )
}
println("Total surface area is $surfaceArea")