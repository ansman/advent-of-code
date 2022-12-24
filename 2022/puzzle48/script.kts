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

data class Position(val x: Int, val y: Int) {
    fun offset(dx: Int, dy: Int): Position = copy(x = x + dx, y = y + dy)
}

enum class HorizontalDirection {
    Left,
    Right,
}

enum class VerticalDirection() {
    Up,
    Down,
}

data class Input(
        val startPosition: Position,
        val endPosition: Position,
        // Maps y -> startX -> direction
        val horizontalWinds: Map<Int, Map<Int, HorizontalDirection>>,
        // Maps x -> startY -> direction
        val verticalWindws: Map<Int, Map<Int, VerticalDirection>>,
        val width: Int,
        val height: Int
) {
    val cycleLength = run {
        when {
            width % height == 0 -> width
            height % width == 0 -> height
            else -> width * height
        }
    }
}

fun readInput(): Input {
    val line1 = readln().removeSurrounding("#")
    val startX = line1.indexOf('.')
    val width = line1.length
    var height = 0
    val horizontalWinds = mutableMapOf<Int, MutableMap<Int, HorizontalDirection>>()
    val verticalWindws = mutableMapOf<Int, MutableMap<Int, VerticalDirection>>()
    var endX = 0
    while (true) {
        val line = readlnOrNull()?.removeSurrounding("#") ?: break
        val y = height++
        line.forEachIndexed { x, c ->
            when (c) {
                '<' -> horizontalWinds.getOrPut(y, ::mutableMapOf).put(x, HorizontalDirection.Left)
                '>' -> horizontalWinds.getOrPut(y, ::mutableMapOf).put(x, HorizontalDirection.Right)
                '^' -> verticalWindws.getOrPut(x, ::mutableMapOf).put(y, VerticalDirection.Up)
                'v' -> verticalWindws.getOrPut(x, ::mutableMapOf).put(y, VerticalDirection.Down)
                '.' -> endX = x
                '#' -> {}
                else -> error("Unknown character $c")
            }
        }
    }
    // Subtracts the last row which we don't count
    --height
    return Input(
            startPosition = Position(startX, -1),
            endPosition = Position(endX, height),
            horizontalWinds = horizontalWinds,
            verticalWindws = verticalWindws,
            width = width,
            height = height
    )
}

data class Step(
        val position: Position,
        val steps: Int,
        val hasVisitedEnd: Boolean,
        val hasGottenSnacks: Boolean,
) {
    private val cycle = steps % input.cycleLength
    private val hashCode = ((position.hashCode() * 31 + cycle) * 31 + hasVisitedEnd.hashCode()) * 31 + hasGottenSnacks.hashCode()

    init {
        if (hasGottenSnacks) {
            require(hasVisitedEnd)
        }
    }
    fun next(
            position: Position,
            hasVisitedEnd: Boolean = this.hasVisitedEnd,
            hasGottenSnacks: Boolean = this.hasGottenSnacks
    ) =
            copy(position = position, steps = steps + 1, hasVisitedEnd = hasVisitedEnd, hasGottenSnacks = hasGottenSnacks)

    override fun hashCode(): Int = hashCode

    override fun equals(other: Any?): Boolean =
            other === this || other is Step &&
                    position == other.position &&
                    cycle == other.cycle &&
                    hasVisitedEnd == other.hasVisitedEnd &&
                    hasGottenSnacks == other.hasGottenSnacks
}

fun Input.countSteps(): Int {
    val queue = ArrayDeque<Step>()
    val seen = mutableSetOf<Step>()
    queue.add(Step(startPosition, 0, hasVisitedEnd = false, hasGottenSnacks = false))
    while (true) {
        val step = queue.removeFirst()
        log { "Visiting step $step" }
        if (step.hasGottenSnacks && step.position == endPosition) return step.steps
        if (!seen.add(step)) continue
        fun enqueue(dx: Int = 0, dy: Int = 0) {
            val x = step.position.x + dx
            val y = step.position.y + dy
            val steps = step.steps + 1
            if (x == startPosition.x && y == startPosition.y) {
                log("Enqueing start position at steps $steps")
                if (step.hasVisitedEnd && !step.hasGottenSnacks) {
                    queue.clear()
                }
                queue.add(step.next(startPosition, hasGottenSnacks = step.hasVisitedEnd))
                return
            } else if (x == endPosition.x && y == endPosition.y) {
                log("Enqueing end position at steps $steps")
                if (step.hasGottenSnacks || !step.hasVisitedEnd) {
                    queue.clear()
                }
                queue.add(step.next(endPosition, hasVisitedEnd = true))
                return
            }

            if (x !in 0 until width || y !in 0 until height) {
                return
            }
            log { " Enqueuing $x,$y at steps $steps" }
            val leftGoingWindX = (x + steps) % width
            val rightGoingWindX = ((x - steps) % width + width) % width
            val upGoingWindY = (y + steps) % height
            val downGoingWindY = ((y - steps) % height + height) % height

            if (horizontalWinds[y]?.get(rightGoingWindX) == HorizontalDirection.Right) {
                log("  It'll hit a right going wind")
                return
            }
            if (horizontalWinds[y]?.get(leftGoingWindX) == HorizontalDirection.Left) {
                log("  It'll hit a left going wind")
                return
            }
            if (verticalWindws[x]?.get(upGoingWindY) == VerticalDirection.Up) {
                log("  It'll hit a up going wind")
                return
            }
            if (verticalWindws[x]?.get(downGoingWindY) == VerticalDirection.Down) {
                log("  It'll hit a down going wind")
                return
            }
            queue.add(step.next(Position(x, y)))
        }
        enqueue(dx = -1)
        enqueue(dx = 1)
        enqueue(dy = -1)
        enqueue(dy = 1)
        enqueue()
        log("")
    }
}

val input = readInput()
log(input)
val steps = input.countSteps()
println("Steps: $steps")
