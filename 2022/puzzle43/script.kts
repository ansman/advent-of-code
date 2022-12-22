#!/usr/bin/env kotlin

import java.lang.Integer.parseInt
import kotlin.script.dependencies.ScriptContents
import kotlin.time.Duration.Companion.nanoseconds

val start = System.nanoTime()
Runtime.getRuntime().addShutdownHook(Thread {
    println("Total runtime ${(System.nanoTime() - start).nanoseconds}")
})

val logging = false

fun log(msg: () -> Any?) {
    if (logging) {
        println(msg())
    }
}

enum class Node(val symbol: Char) {
    Tile('.'),
    Wall('#'),
}

fun readMap(): Map<Int, Map<Int, Node>> = buildMap {
    var y = 0
    while (true) {
        val line = readLine()!!
        if (line.isEmpty()) break
        put(y, buildMap {
            var x = line.indexOfFirst { it != ' ' }
            while (x < line.length) {
                put(x, requireNotNull(Node.values().find { it.symbol == line[x] }) { "Unknown symbol ${line[x]}" })
                ++x
            }
        })
        ++y
    }
}

data class Coordinate(val x: Int, val y: Int)
enum class Facing(val dx: Int, val dy: Int, val symbol: Char) {
    Right(1, 0, '>'),
    Down(0, 1, 'v'),
    Left(-1, 0, '<'),
    Up(0, -1, '^');

    fun rotate(direction: Action.Rotate.Direction): Facing {
        val offset = when (direction) {
            Action.Rotate.Direction.Clockwise -> 1
            Action.Rotate.Direction.CounterClockwise -> -1
        }
        val values = values()
        var nextEnum = ordinal + offset
        if (nextEnum < 0) {
           nextEnum += values.size
        }
        return values[nextEnum % values.size]
    }
}

sealed class Action {
    companion object {
        fun parse(input: String): List<Action> = buildList {
            var i = 0
            while (i < input.length) {
                val start = i
                while (i < input.length && input[i].isDigit()) {
                    ++i
                }
                if (i != start) {
                    add(Move(parseInt(input, start, i, 10)))
                }
                if (i >= input.length) {
                    continue
                }
                add(Rotate(when (val c = input[i++]) {
                    'R' -> Rotate.Direction.Clockwise
                    'L' -> Rotate.Direction.CounterClockwise
                    else -> error("Unknown rotation $c")
                }))
            }
        }
    }

    data class Rotate(val direction: Direction) : Action() {
        enum class Direction {
            Clockwise,
            CounterClockwise
        }
    }

    data class Move(val steps: Int) : Action()
}

val map = readMap()
val path = Action.parse(readLine()!!)
val visited = mutableMapOf<Int, MutableMap<Int, Facing>>()

fun drawMap() {
    if (!logging) return
    for ((y, row) in map) {
        repeat(row.keys.first()) {
            print(' ')
        }
        for ((x, tile) in row) {
            val facing = visited[y]?.get(x)
            print(facing?.symbol ?: tile.symbol)
        }
        println()
    }
    println()
}

fun visit(x: Int, y: Int, facing: Facing) {
    if (!logging) return
    visited.getOrPut(y, ::mutableMapOf).put(x, facing)
}

drawMap()
var y = 0
var x = map.getValue(y).keys.first()
var facing = Facing.Right
visit(x, y, facing)
for (action in path) {
    log { "Perfoming action $action" }
    when (action) {
        is Action.Rotate -> {
            facing = facing.rotate(action.direction)
            visit(x, y, facing)
        }
        is Action.Move -> {
            for (i in 0 until action.steps) {
                var newX = x + facing.dx
                var newY = y + facing.dy
                val tile = map[newY]?.get(newX) ?: run {
                    // Wrap around
                    when (facing) {
                        Facing.Right -> newX = 0
                        Facing.Down -> newY = 0
                        Facing.Left -> newX = map.getValue(y).keys.last()
                        Facing.Up -> newY = map.size - 1
                    }
                    // Step until we find the next position
                    while (map[newY]?.get(newX) == null) {
                        newX += facing.dx
                        newY += facing.dy
                    }
                    map.getValue(newY).getValue(newX)
                }
                when (tile) {
                    Node.Tile -> {
                        x = newX
                        y = newY
                        visit(x, y, facing)
                    }
                    Node.Wall -> {
                        // Cannot go further
                        break
                    }
                }
            }
        }
    }

    drawMap()
}

val score = (y + 1) * 1000 +
        (x + 1) * 4 +
        facing.ordinal
println("Score is $score")