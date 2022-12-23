#!/usr/bin/env kotlin

import kotlin.time.Duration.Companion.nanoseconds

val start = System.nanoTime()
Runtime.getRuntime().addShutdownHook(Thread {
    println("Total runtime ${(System.nanoTime() - start).nanoseconds}")
})

val logging = false

fun log(msg: Any?) = log { msg }
fun log(msg: () -> Any?) {
    if (logging) {
        println(msg())
    }
}

data class Offset(val dx: Int, val dy: Int)
data class Position(val x: Int, val y: Int)

enum class Direction(
        val moveDirection: Offset,
        val adjacent1: Offset,
        val adjacent2: Offset,
) {
    North(Offset(0, -1), Offset(1, -1), Offset(-1, -1)),
    South(Offset(0, 1), Offset(1, 1), Offset(-1, 1)),
    West(Offset(-1, 0), Offset(-1, -1), Offset(-1, 1)),
    East(Offset(1, 0), Offset(1, -1), Offset(1, 1));

    private val directions = arrayOf(moveDirection, adjacent1, adjacent2)
    fun tryMove(elfs: Map<Int, Set<Int>>, x: Int, y: Int): Position? =
            if (canMove(elfs, x, y)) {
                Position(x + moveDirection.dx, y + moveDirection.dy)
            } else {
                null
            }

    fun canMove(elfs: Map<Int, Set<Int>>, x: Int, y: Int): Boolean =
            directions.none { offset -> elfs[x + offset.dx]?.contains(y + offset.dy) ?: false }
}

val elfs = mutableMapOf<Int, MutableSet<Int>>()
run {
    var y = 0
    while (true) {
        val line = readLine() ?: break
        line.forEachIndexed { x, c ->
            if (c == '#') {
                elfs.getOrPut(x, ::mutableSetOf).add(y)
            }
        }
        ++y
    }
}


val minX: Int get() = elfs.keys.min()
val maxX: Int get() = elfs.keys.max()
val minY: Int get() = elfs.values.asSequence().mapNotNull { it.minOrNull() }.min()
val maxY: Int get() = elfs.values.asSequence().mapNotNull { it.maxOrNull() }.max()

fun drawGrid() {
    if (!logging) return
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            print(if (elfs[x]?.contains(y) == true) {
                '#'
            } else {
                '.'
            })
        }
        println()
    }
    println()
}

drawGrid()
var round = 0
while (true) {
    val directions = Direction.values()
    val occupiedPositions = mutableMapOf<Position, Int>()
    val proposals = mutableMapOf<Int, MutableMap<Int, Position>>()
    for ((x, col) in elfs) {
        for (y in col) {
            if (directions.all { it.canMove(elfs, x, y) }) {
                continue
            }

            for (dir in 0 until directions.size) {
                val direction = directions[(dir + round) % directions.size]
                val newPosition = direction.tryMove(elfs, x, y) ?: continue
                occupiedPositions[newPosition] = occupiedPositions.getOrDefault(newPosition, 0) + 1
                proposals.getOrPut(x, ::mutableMapOf).put(y, newPosition)
                break
            }
        }
    }
    for ((x, col) in proposals) {
        for ((y, position) in col) {
            if (occupiedPositions[position] != 1) {
                continue
            }
            elfs.getValue(x).remove(y)
            elfs.getOrPut(position.x, ::mutableSetOf).add(position.y)
        }
    }
    drawGrid()
    ++round
    if (proposals.isEmpty()) {
        break
    }
}
println("Rounds until no elf moved: $round")