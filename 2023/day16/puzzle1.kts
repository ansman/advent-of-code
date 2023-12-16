#!/usr/bin/env kotlin

import Puzzle1.Beam.Direction.*
import java.util.*
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

val grid = generateSequence(::readlnOrNull).toList()

data class Beam(val x: Int, val y: Int, val direction: Direction) {
    val position = (x.toLong() shl 32) or y.toLong()

    fun step(): Beam = copy(x = x + direction.dx, y = y + direction.dy)

    fun reflect(mirror: Char): Beam {
        val newDirection = when (mirror) {
            '/' -> when (direction) {
                Up -> Right
                Down -> Left
                Left -> Down
                Right -> Up
            }

            '\\' -> when (direction) {
                Up -> Left
                Down -> Right
                Left -> Up
                Right -> Down
            }
            else -> error("Unknown mirror: $mirror")
        }
        return copy(direction = newDirection).step()
    }

    fun split(reflector: Char): List<Beam> {
        val directions = when (direction) {
            Left, Right -> {
                when (reflector) {
                    '-' -> listOf(direction)
                    '|' -> listOf(Up, Down)
                    else -> error("Unknown reflector: $reflector")
                }
            }

            Up, Down -> {
                when (reflector) {
                    '-' -> listOf(Left, Right)
                    '|' -> listOf(direction)
                    else -> error("Unknown reflector: $reflector")
                }
            }
        }
        return directions.map { copy(direction = it).step() }
    }

    fun isInBounds(width: Int, height: Int): Boolean = x in 0 until width && y in 0 until height

    enum class Direction(val dx: Int, val dy: Int) {
        Up(0, -1),
        Down(0, 1),
        Left(-1, 0),
        Right(1, 0);

        val isVertical get() = this == Up || this == Down
    }
}

val beams = LinkedList(listOf(Beam(0, 0, Beam.Direction.Right)))
val energized = mutableSetOf<Long>()
val seen = mutableSetOf<Beam>()
while (beams.isNotEmpty()) {
    val beam = beams.removeFirst()
    if (!beam.isInBounds(grid[0].length, grid.size)) {
        continue
    }
    if (!seen.add(beam)) {
        continue
    }
    energized.add(beam.position)
    val (x, y) = beam
    when (val c = grid[y][x]) {
        '.' -> beams.add(beam.step())
        '/', '\\' -> beams.add(beam.reflect(c))
        '-', '|' -> beams.addAll(beam.split(c))
        else -> error("Unknown character: $c")
    }
}

println(energized.size)

println(start.elapsedNow())