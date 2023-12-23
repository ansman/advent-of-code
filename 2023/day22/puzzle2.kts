#!/usr/bin/env kotlin

import java.util.PriorityQueue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.system.exitProcess
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
    val minZ get() = min(start.z, end.z)
    val maxZ get() = max(start.z, end.z)

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
    private val byZ = mutableMapOf<Int, MutableSet<Brick>>()
    private val grid = mutableMapOf<Int, MutableMap<Int, MutableMap<Int, Brick>>>()

    override fun iterator(): Iterator<Brick> = grid.values
            .asSequence()
            .flatMap { it.values.asSequence() }
            .flatMap { it.values }
            .distinct()
            .iterator()

    fun bricksAt(z: Int) = byZ[z] ?: emptySet()

    fun add(brick: Brick) {
        for (xyz in brick) {
            byZ.getOrPut(xyz.z, ::mutableSetOf).add(brick)
            val prev = grid.getOrPut(xyz.z, ::mutableMapOf).getOrPut(xyz.y, ::mutableMapOf).put(xyz.x, brick)
            require(prev == null) {
                "Brick $prev and $brick both occupy $xyz"
            }
        }
    }

    fun settle() {
        val sortedBricks = sortedBy { min(it.start.z, it.end.z) }
        grid.clear()
        byZ.clear()
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

    fun canSafelyBeRemoved(brick: Brick): Boolean = supportedBricks(brick).none()

    fun supportedBricks(brick: Brick, removed: Set<Brick> = emptySet()): Sequence<Brick> =
            brick.adjacentBricks(dz = 1, removed).filter { adjacent ->
                isOnlySupportedBy(adjacent, brick, removed)
            }

    fun isOnlySupportedBy(brick: Brick, supportingBrick: Brick, removed: Set<Brick>): Boolean =
            brick.adjacentBricks(dz = -1, removed).filter { it != supportingBrick }.none()

    private fun Brick.adjacentBricks(dz: Int, removed: Set<Brick>): Sequence<Brick> =
            offset(dz = dz).asSequence().mapNotNull { brickAt(it) }.filter { it != this && it !in removed }

    fun computeFallingBricks(brick: Brick): Int {
        val removed = mutableSetOf(brick)
        while (true) {
            val unsupported = unsupportedBricks(removed)
            if (unsupported.isEmpty()) {
                break
            }
            removed.addAll(unsupported)
        }
        return removed.size - 1
    }

    private fun unsupportedBricks(removed: Set<Brick>): Set<Brick> =
            filterTo(mutableSetOf()) { it !in removed && !it.isSupported(removed) }

    fun Brick.isSupported(removed: Set<Brick>): Boolean =
            any { cell ->
                cell.z == 1 || brickAt(cell.offset(dz = -1))?.takeUnless { it == this || it in removed }  != null
            }

    fun assertSettled() {
        for (brick in this) {
            check(brick.isSupported(emptySet())) {
                "Brick ${brick.name} can sink"
            }
        }
    }
}

val grid = Grid()

generateSequence(::readlnOrNull)
        .map { line -> line.split('~').map { XYZ.parse(it) } }
        .map { (start, end) -> Brick(start, end) }
        .forEach { grid.add(it) }

grid.settle()
if (debug) grid.assertSettled()
if (debug) println("Total bricks: ${grid.count()}")

grid.filter { !grid.canSafelyBeRemoved(it) }
        .sumOf { brick ->
            grid.computeFallingBricks(brick).also {
               if (debug) println("Brick ${brick.name} supports $it bricks")
            }
        }
        .also { println(it) }

println(start.elapsedNow())