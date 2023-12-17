#!/usr/bin/env kotlin

import Puzzle2.Path.Direction.*
import java.util.*
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

data class Path(
        val x: Int,
        val y: Int,
        val stepsInDirection: Int,
        val direction: Direction,
        val heatLoss: Int,
        val prev: List<Path> = emptyList(),
) {
    val position = (x.toLong() shl 32) or y.toLong()

    override fun hashCode(): Int {
        var hashCode = 1
        hashCode = 31 * hashCode + x
        hashCode = 31 * hashCode + y
        hashCode = 31 * hashCode + stepsInDirection
        hashCode = 31 * hashCode + direction.hashCode()
        return hashCode
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Path) return false
        return x == other.x && y == other.y && stepsInDirection == other.stepsInDirection && direction == other.direction
    }

    fun step(grid: List<List<Int>>): Sequence<Path> = sequence {
        suspend fun SequenceScope<Path>.yield(direction: Direction) {
            val newX = x + direction.dx
            val newY = y + direction.dy
            if (newY in grid.indices && newX in grid[newY].indices) {
                yield(Path(
                        x = newX,
                        y = newY,
                        stepsInDirection = if (direction == this@Path.direction) stepsInDirection + 1 else 1,
                        direction = direction,
                        heatLoss = heatLoss + grid[newY][newX],
                        prev = if (debug) prev + this@Path else emptyList(),
                ))
            }
        }

        if (stepsInDirection < 10) {
            yield(direction)
        }
        if (stepsInDirection >= 4) {
            yield(Direction.entries[(Direction.entries.size + direction.ordinal - 1) % Direction.entries.size])
            yield(Direction.entries[(direction.ordinal + 1) % Direction.entries.size])
        }
    }

    enum class Direction(val dx: Int, val dy: Int) {
        Left(-1, 0),
        Up(0, -1),
        Right(1, 0),
        Down(0, 1),
    }
}

val grid = generateSequence(::readlnOrNull)
        .map { ln -> ln.map { it.digitToInt() } }
        .toList()
//
//val visited = Array(grid.size) { BooleanArray(grid[it].size) }
//val distances = Array(grid.size) { IntArray(grid[it].size) { Int.MAX_VALUE } }
//val

val queue = PriorityQueue<Path>(compareBy { it.heatLoss })
queue.add(Path(0, 0, 0, Right, 0))
queue.add(Path(0, 0, 0, Down, 0))
val seen = queue.toMutableSet()
while (true) {
    val path = queue.poll()
    if (path.x == grid[0].lastIndex && path.y == grid.lastIndex && path.stepsInDirection >= 4) {
        if (debug) {
            val g = grid.map { it.map { d -> d.toString() }.toMutableList() }.toMutableList()
            for (p in path.prev + path) {

                g[p.y][p.x] = when (p.direction) {
                    Left -> "<"
                    Up -> "^"
                    Right -> ">"
                    Down -> "v"
                }
            }
            g.forEach { println(it.joinToString("")) }
        }
        println(path.heatLoss)
        break
    }
    queue.addAll(path.step(grid).filter { seen.add(it) })
}

println(start.elapsedNow())