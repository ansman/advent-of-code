#!/usr/bin/env kotlin

import Script.Action.Rotate.Direction
import Script.State
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

fun readMap(): List<Map<Int, Node>> = buildList {
    var y = 0
    while (true) {
        val line = readLine()!!
        if (line.isEmpty()) break
        add(buildMap {
            var x = line.indexOfFirst { it != ' ' }
            while (x < line.length) {
                put(x, requireNotNull(Node.values().find { it.symbol == line[x] }) { "Unknown symbol ${line[x]}" })
                ++x
            }
        })
        ++y
    }
}

fun List<Map<Int, Node>>.exists(coordinate: Coordinate2D): Boolean = getOrNull(coordinate.y)?.get(coordinate.x) != null
fun Map<Int, Map<Int, Map<Int, State>>>.exists(coordinate: Coordinate3D): Boolean =
        get(coordinate.x)?.get(coordinate.y)?.get(coordinate.z) != null

data class Coordinate2D(val x: Int, val y: Int) {
    fun offset(dx: Int = 0, dy: Int = 0) = copy(x = x + dx, y = y + dy)
    override fun toString(): String = "$x,$y"
}

data class Coordinate3D(val x: Int, val y: Int, val z: Int) {
    fun offset(facing: Facing3D) = copy(x = x + facing.dx, y = y + facing.dy, z = z + facing.dz)
    override fun toString(): String = "$x,$y,$z"
}

enum class Face {
    Front,
    Top,
    Back,
    Bottom,
    Left,
    Right;

    fun wrap(position: Coordinate3D, facing: Facing3D): Triple<Face, Coordinate3D, Facing3D> {
        var (x, y, z) = position
        val newFace: Face = when (facing) {
            Facing3D.Right -> Right
            Facing3D.Down -> Bottom
            Facing3D.Left -> Left
            Facing3D.Up -> Top
            Facing3D.Forwards -> Back
            Facing3D.Backwards -> Front
        }
        val newFacing: Facing3D = when (this) {
            Front -> Facing3D.Forwards
            Top -> Facing3D.Down
            Back -> Facing3D.Backwards
            Bottom -> Facing3D.Up
            Left -> Facing3D.Right
            Right -> Facing3D.Left
        }
        x += newFacing.dx
        y += newFacing.dy
        z += newFacing.dz
        return Triple(newFace, Coordinate3D(x, y, z), newFacing)
    }
}

enum class Facing2D(val symbol: Char) {
    Right('>'),
    Down('v'),
    Left('<'),
    Up('^');

    fun rotate(direction: Action.Rotate.Direction): Facing2D {
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


enum class Facing3D(val dx: Int, val dy: Int, val dz: Int) {
    Right(1, 0, 0),
    Down(0, 1, 0),
    Left(-1, 0, 0),
    Up(0, -1, 0),
    Forwards(0, 0, 1),
    Backwards(0, 0, -1);

    fun rotate(direction: Direction, face: Face): Facing3D = when (face) {
        Face.Front -> when (this) {
            Right -> when (direction) {
                Direction.Clockwise -> Down
                Direction.CounterClockwise -> Up
            }

            Down -> when (direction) {
                Direction.Clockwise -> Left
                Direction.CounterClockwise -> Right
            }

            Left -> when (direction) {
                Direction.Clockwise -> Up
                Direction.CounterClockwise -> Down
            }

            Up -> when (direction) {
                Direction.Clockwise -> Right
                Direction.CounterClockwise -> Left
            }

            Forwards, Backwards -> error("Cannot be")
        }

        Face.Top -> when (this) {
            Right -> when (direction) {
                Direction.Clockwise -> Backwards
                Direction.CounterClockwise -> Forwards
            }

            Left -> when (direction) {
                Direction.Clockwise -> Forwards
                Direction.CounterClockwise -> Backwards
            }

            Forwards -> when (direction) {
                Direction.Clockwise -> Right
                Direction.CounterClockwise -> Left
            }

            Backwards -> when (direction) {
                Direction.Clockwise -> Left
                Direction.CounterClockwise -> Right
            }

            Down, Up -> error("Cannot be")
        }

        Face.Back -> when (this) {
            Right -> when (direction) {
                Direction.Clockwise -> Up
                Direction.CounterClockwise -> Down
            }

            Down -> when (direction) {
                Direction.Clockwise -> Right
                Direction.CounterClockwise -> Left
            }

            Left -> when (direction) {
                Direction.Clockwise -> Down
                Direction.CounterClockwise -> Up
            }

            Up -> when (direction) {
                Direction.Clockwise -> Left
                Direction.CounterClockwise -> Right
            }

            Forwards, Backwards -> error("Cannot be")
        }

        Face.Bottom -> when (this) {
            Right -> when (direction) {
                Direction.Clockwise -> Forwards
                Direction.CounterClockwise -> Backwards
            }

            Left -> when (direction) {
                Direction.Clockwise -> Backwards
                Direction.CounterClockwise -> Forwards
            }

            Forwards -> when (direction) {
                Direction.Clockwise -> Left
                Direction.CounterClockwise -> Right
            }

            Backwards -> when (direction) {
                Direction.Clockwise -> Right
                Direction.CounterClockwise -> Left
            }

            Down, Up -> error("Cannot be")
        }

        Face.Left -> when (this) {
            Up -> when (direction) {
                Direction.Clockwise -> Backwards
                Direction.CounterClockwise -> Forwards
            }

            Down -> when (direction) {
                Direction.Clockwise -> Forwards
                Direction.CounterClockwise -> Backwards
            }

            Forwards -> when (direction) {
                Direction.Clockwise -> Up
                Direction.CounterClockwise -> Down
            }

            Backwards -> when (direction) {
                Direction.Clockwise -> Down
                Direction.CounterClockwise -> Up
            }

            Left, Right -> error("Cannot be")
        }

        Face.Right -> when (this) {
            Up -> when (direction) {
                Direction.Clockwise -> Forwards
                Direction.CounterClockwise -> Backwards
            }

            Down -> when (direction) {
                Direction.Clockwise -> Backwards
                Direction.CounterClockwise -> Forwards
            }

            Forwards -> when (direction) {
                Direction.Clockwise -> Down
                Direction.CounterClockwise -> Up
            }

            Backwards -> when (direction) {
                Direction.Clockwise -> Up
                Direction.CounterClockwise -> Down
            }

            Left, Right -> error("Cannot be")
        }
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
                    'R' -> Direction.Clockwise
                    'L' -> Direction.CounterClockwise
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

val verticalFaces = mutableListOf<IntRange>().apply {
    var y = 0
    var lastFaceStart = y
    var cw = map[y].size
    while (y < map.size) {
        val w = map[y].size
        if (w != cw) {
            add(lastFaceStart until y)
            cw = w
            lastFaceStart = y
        }
        ++y
    }
    add(lastFaceStart until y)
}

val horizontalFaces = mutableListOf<IntRange>().apply {
    var x = 0
    var lastFaceStart = x
    fun currentHeight(): Int = map.count { x in it }

    var ch = currentHeight()
    val width = map.maxOf { it.keys.max() }
    while (x <= width) {
        val h = currentHeight()
        if (h != ch) {
            add(lastFaceStart until x)
            ch = h
            lastFaceStart = x
        }
        ++x
    }
    add(lastFaceStart until x)
}

val IntRange.length: Int get() = endInclusive - start + 1

if (horizontalFaces.size == 3 && verticalFaces.size == 3) {
    // We need to split one of them
    val left = horizontalFaces.first()
    val right = horizontalFaces.last()
    if (left.length != right.length) {
        if (left.length > right.length) {
            // We need to split the left face
            val l1 = left.start..(left.endInclusive - right.length)
            val l2 = (l1.endInclusive + 1)..left.endInclusive
            horizontalFaces.set(0, l1)
            horizontalFaces.add(1, l2)
        } else {
            // We need to split the right face
            val r1 = right.start until (right.start + left.length)
            val r2 = (r1.endInclusive + 1)..right.endInclusive
            horizontalFaces.set(horizontalFaces.lastIndex, r1)
            horizontalFaces.add(r2)
        }
    } else {
        val top = verticalFaces.first()
        val bottom = verticalFaces.last()
        check(top.length != bottom.length)
        if (top.length > bottom.length) {
            // We need to split the top face
            val t1 = top.start..(top.endInclusive - bottom.length)
            val t2 = (t1.endInclusive + 1)..top.endInclusive
            verticalFaces.set(0, t1)
            verticalFaces.add(1, t2)
        } else {
            // We need to split the bottom face
            val b1 = bottom.start until (bottom.start + top.length)
            val b2 = (b1.endInclusive + 1)..bottom.endInclusive
            verticalFaces.set(verticalFaces.lastIndex, b1)
            verticalFaces.add(b2)
        }
    }
}

// x, y, z to State

data class State(
        val coordinate2D: Coordinate2D,
        val coordinate3D: Coordinate3D,
        val face: Face,
        val rightFacing: Facing3D,
        val horizontalFace: IntRange,
        val verticalFace: IntRange,
) {
    val node: Node get() = map[coordinate2D.y].getValue(coordinate2D.x)
    fun facing2D(facing: Facing3D): Facing2D {
        var facing2D = Facing2D.Right
        var facing3D = rightFacing
        repeat(4) {
            if (facing3D == facing) {
                return facing2D
            }
            facing3D = facing3D.rotate(Direction.Clockwise, face)
            facing2D = facing2D.rotate(Direction.Clockwise)
        }
        error("Could not rotate")
    }

    constructor(coordinate2D: Coordinate2D) : this(
            coordinate2D = coordinate2D,
            coordinate3D = Coordinate3D(0, 0, 0),
            face = Face.Front,
            rightFacing = Facing3D.Right,
            horizontalFace = horizontalFaces.first { coordinate2D.x in it },
            verticalFace = verticalFaces.first()
    )
}

val map3D = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, State>>>()
val stateBy2D = mutableMapOf<Int, MutableMap<Int, State>>()

// Runs a BFS which maps the 2d map to a 3d cube
run {
    val queue = ArrayDeque<State>()
    queue.add(State(Coordinate2D(x = map[0].keys.first(), y = 0)))
    val seen = mutableSetOf<Coordinate2D>()
    while (queue.isNotEmpty()) {
        val state = queue.removeFirst()
        if (state.coordinate2D in seen) continue
        fun enqueue(
                next2D: Coordinate2D.() -> Coordinate2D,
                rotations: Int
        ) {
            val next2D = state.coordinate2D.next2D()
            if (!map.exists(next2D) || next2D in seen) return
            val horizontalFace = horizontalFaces.first { next2D.x in it }
            val verticalFace = verticalFaces.first { next2D.y in it }
            var facing = state.rightFacing
            repeat(rotations) {
                facing = facing.rotate(Direction.Clockwise, state.face)
            }
            queue.add(if (horizontalFace == state.horizontalFace && verticalFace == state.verticalFace) {
                state.copy(
                        coordinate2D = next2D,
                        coordinate3D = state.coordinate3D.offset(facing),
                )
            } else {
                var (nextFace, next3D, nextFacing) = state.face.wrap(state.coordinate3D.offset(facing), facing)
                repeat(rotations) {
                    nextFacing = nextFacing.rotate(Direction.CounterClockwise, nextFace)
                }
                State(
                        coordinate2D = next2D,
                        coordinate3D = next3D,
                        face = nextFace,
                        rightFacing = nextFacing,
                        horizontalFace = horizontalFace,
                        verticalFace = verticalFace
                )
            })
        }

        seen.add(state.coordinate2D)
        val (x, y, z) = state.coordinate3D
        require(map3D
                .getOrPut(x, ::mutableMapOf)
                .getOrPut(y, ::mutableMapOf)
                .put(z, state) == null)
        require(stateBy2D
                .getOrPut(state.coordinate2D.x, ::mutableMapOf)
                .put(state.coordinate2D.y, state) == null)

        enqueue(next2D = { offset(dx = 1) }, rotations = 0)
        enqueue(next2D = { offset(dy = 1) }, rotations = 1)
        enqueue(next2D = { offset(dx = -1) }, rotations = 2)
    }
}

// This prints the faces
if (logging) {
    map.forEachIndexed { y, row ->
        repeat(row.keys.first()) {
            print(' ')
        }
        for (x in row.keys) {
            print(when (stateBy2D.getValue(x).getValue(y).face) {
                Face.Front -> "F"
                Face.Top -> "T"
                Face.Back -> "B"
                Face.Bottom -> "U"
                Face.Left -> "L"
                Face.Right -> "R"
            })
        }
        println()
    }
    println()
}

val visited = mutableMapOf<Int, MutableMap<Int, Facing2D>>()

fun drawMap() {
    if (!logging) return
    map.forEachIndexed { y, row ->
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

var position = Coordinate3D(0, 0, 0)
var facing = Facing3D.Right
var face = Face.Front
val currentState: State get() = map3D.getValue(position.x).getValue(position.y).getValue(position.z)

fun logVisit() {
    if (!logging) return
    val state = currentState
    val facing2D = state.facing2D(facing)
    visited.getOrPut(state.coordinate2D.y, ::mutableMapOf).put(state.coordinate2D.x, facing2D)
}

drawMap()
logVisit()
for (action in path) {
    log { "Executing action $action" }
    when (action) {
        is Action.Rotate -> {
            log { "Rotating on $facing, ${action.direction}" }
            facing = facing.rotate(action.direction, face)
            log { "Now facing $facing" }
            logVisit()
            drawMap()
        }
        is Action.Move -> for (i in 0 until action.steps) {
            var newPosition = position.offset(facing)
            var newFace = face
            var newFacing = facing
            log { "Stepping from $position to $newPosition on face $face" }
            if (!map3D.exists(newPosition)) {
                log { "Wrapping, now on face $face, facing $facing" }
                val wrapped = face.wrap(newPosition, facing)
                newFace = wrapped.first
                newPosition = wrapped.second
                newFacing = wrapped.third
            }

            when (map3D.getValue(newPosition.x).getValue(newPosition.y).getValue(newPosition.z).node) {
                Node.Tile -> {
                    face = newFace
                    position = newPosition
                    facing = newFacing
                    log { currentState }
                }

                Node.Wall -> {
                    log { "Wall hit, stopping" }
                    log { "" }
                    // Cannot go further
                    break
                }
            }
            logVisit()
            drawMap()
        }
    }
}


val endPosition2D = map3D.getValue(position.x).getValue(position.y).getValue(position.z).coordinate2D
val score = (endPosition2D.y + 1) * 1000 +
        (endPosition2D.x + 1) * 4 +
        currentState.facing2D(facing).ordinal
println("Score is $score")