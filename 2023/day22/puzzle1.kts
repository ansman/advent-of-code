#!/usr/bin/env kotlin

import kotlin.math.min
import kotlin.math.sign
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

data class XYZ(val x: Int, val y: Int, val z: Int) {
    fun offset(dx: Int = 0, dy: Int = 0, dz: Int = 0): XYZ = XYZ(x + dx, y + dy, z + dz)

    companion object {
        fun parse(s: String): XYZ {
            val (x, y, z) = s.split(',')
            return XYZ(x.toInt(), y.toInt(), z.toInt())
        }
    }
}

data class Brick(val start: XYZ, val end: XYZ, val name: String = (nextName++).toString()) : Iterable<XYZ> {
    private val cells = sequence {
        val dx = (end.x - start.x).sign
        val dy = (end.y - start.y).sign
        val dz = (end.z - start.z).sign
        var cell = start
        while (true) {
            yield(cell)
            if (cell == end) {
                break
            } else {
                cell = cell.offset(dx, dy, dz)
            }
        }
    }

    override fun iterator(): Iterator<XYZ> = cells.iterator()

    fun offset(dx: Int = 0, dy: Int = 0, dz: Int = 0): Brick =
            copy(start = start.offset(dx, dy, dz), end = end.offset(dx, dy, dz))

    companion object {
        private var nextName = 'A'
    }
}

class Grid : Iterable<Brick> {
    private val grid = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, Brick>>>()

    override fun iterator(): Iterator<Brick> = grid.values
            .asSequence()
            .flatMap { it.values.asSequence() }
            .flatMap { it.values }
            .distinct()
            .iterator()

    fun add(brick: Brick) {
        for (xyz in brick) {
            val prev = grid.getOrPut(xyz.z, ::mutableMapOf).getOrPut(xyz.y, ::mutableMapOf).put(xyz.x, brick)
            require(prev == null) {
                "Brick $prev and $brick both occupy $xyz"
            }
        }
    }

    fun settle() {
        val sortedBricks = sortedBy { min(it.start.z, it.end.z) }
        grid.clear()
        for (brick in sortedBricks) {
            add(brick.settle())
        }
    }

    private fun Brick.settle(): Brick {
        var settled = this
        while (true) {
            val b = settled.offset(dz = -1)
            if (b.canFit()) {
                settled = b
            } else {
                break
            }
        }
        return settled
    }

    private fun brickAt(xyz: XYZ): Brick? = grid[xyz.z]?.get(xyz.y)?.get(xyz.x)
    private fun isOccupied(xyz: XYZ): Boolean = brickAt(xyz) != null

    private fun Brick.canFit(): Boolean = all { it.z > 0 && !isOccupied(it) }

    fun canSafelyBeRemoved(brick: Brick): Boolean =
            // All the bricks above must have at least one other bricks below
            brick.adjacentBricks(dz = 1).all { adjacent ->
                if (debug) println("  Brick ${brick.name} is below ${adjacent.name}")
                adjacent.adjacentBricks(dz = -1).filter { it != brick }.any()
            }

    private fun Brick.adjacentBricks(dz: Int): Sequence<Brick> =
            offset(dz = dz).asSequence().mapNotNull { brickAt(it) }.filter { it != this }

    fun assertSettled() {
        for (brick in this) {
            val couldSink = brick.all { cell ->
                cell.z > 1 && !isOccupied(cell.offset(dz = -1))
            }
            require(!couldSink) {
                "Brick $brick could sink"
            }
        }
    }
}

val grid = Grid()

generateSequence(::readlnOrNull)
        .map { line -> line.split('~').map { XYZ.parse(it) } }
        .map { (start, end) -> Brick(start, end) }
        .forEach { grid.add(it) }

fun printXZView() {
    if (!debug) return
    val bricks = grid
            .asSequence()
            .flatMap { brick -> brick.asSequence().map { (it.x to it.z) to brick } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.distinct() }

    val xs = bricks.keys.minOf { it.first }..bricks.keys.maxOf { it.first }
    val ys = bricks.keys.maxOf { it.second } downTo bricks.keys.minOf { it.second }
    repeat((xs.last - xs.first + 1) / 2) { print(' ') }
    println("x")
    for (x in xs) {
        print(x)
    }
    println()
    for (y in ys) {
        for (x in xs) {
            val bs = bricks[x to y] ?: emptyList()
            print(when (bs.size) {
                0 -> '.'
                1 -> bs.single()
                else -> '?'
            })
        }
        print(' ')
        print(y)
        if (y == (ys.first + ys.last + 1) / 2) {
            print(" y")
        }
        println()
    }
}

//printXZView()
grid.settle()
if (debug) grid.assertSettled()
println("Total bricks: ${grid.count()}")
println(grid.count { brick ->
    grid.canSafelyBeRemoved(brick).also {
        if (debug) {
            println("Brick ${brick.name} can safely be removed")
        }
    }
})
println(start.elapsedNow())