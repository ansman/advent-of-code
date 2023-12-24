#!/usr/bin/env kotlin

import java.text.DecimalFormat
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = args.isNotEmpty()

data class XY(val x: Double, val y: Double) {
    override fun toString(): String = "x = ${numberFormatter.format(x)}, y = ${numberFormatter.format(y)}"

    companion object {
        private val numberFormatter = DecimalFormat.getNumberInstance().apply {
            maximumFractionDigits = 3
        }

    }
}

data class XYZ(val x: Long, val y: Long, val z: Long) {
    override fun toString(): String = "$x, $y, $z"

    companion object {
        fun parse(input: String): XYZ {
            val (x, y, z) = input.split(", ").map { it.trim().toLong() }
            return XYZ(x, y, z)

        }
    }
}

data class Hailstone(val position: XYZ, val velocity: XYZ) {
    val path = Path.from(position, velocity)

    fun timeAtX(x: Double): Double = (x - position.x) / velocity.x
    fun timeAtY(y: Double): Double = (y - position.y) / velocity.y

    override fun toString(): String = "$position @ $velocity"

    companion object {
        fun parse(input: String): Hailstone {
            val (position, velocity) = input.split(" @ ").map { XYZ.parse(it) }
            return Hailstone(position, velocity)

        }
    }
}

data class Path(val a: Double, val b: Double, val c: Double) {
    fun intersectionWith(other: Path): XY? {
        val a1 = a
        val b1 = b
        val c1 = c
        val a2 = other.a
        val b2 = other.b
        val c2 = other.c
        val x = (b1 * c2 - b2 * c1) / (a1 * b2 - a2 * b1)
        val y = (c1 * a2 - c2 * a1) / (a1 * b2 - a2 * b1)
        return if (x.isFinite() && y.isFinite()) XY(x, y) else null
    }

    companion object {
        fun from(position: XYZ, velocity: XYZ): Path {
            val x1 = position.x
            val y1 = position.y
            val x2 = position.x + velocity.x
            val y2 = position.y + velocity.y
            val a = (y2 - y1).toDouble()
            val b = (x1 - x2).toDouble()
            val c = (y1 * x2 - x1 * y2).toDouble()
            return Path(a, b, c)
        }
    }
}

val area = (args.getOrNull(0)?.toDouble() ?: 200000000000000.0)..(args.getOrNull(1)?.toDouble() ?: 400000000000000.0)

val hailstones = generateSequence(::readlnOrNull)
        .map { Hailstone.parse(it) }
        .toList()

println(hailstones.indices.sumOf { ai ->
    val a = hailstones[ai]
    hailstones.drop(ai + 1).count { b ->
        if (a === b) {
            false
        } else {
            if (debug) {
                println()
                println("Hailstone A: $a")
                println("Hailstone B: $b")
            }
            val intersection = a.path.intersectionWith(b.path)
            if (intersection == null) {
                if (debug) println("Hailstones' paths are parallel; they never intersect.")
                return@count false
            }

            if (a.timeAtX(intersection.x) < 0) {
                if (debug) println("Hailstones' paths crossed in the past for hailstone A.")
                return@count false
            }
            if (b.timeAtX(intersection.x) < 0) {
                if (debug) println("Hailstones' paths crossed in the past for hailstone B.")
                return@count false
            }

            if (intersection.x in area && intersection.y in area) {
                if (debug) println("Hailstones' paths will cross inside the test area (at $intersection).")
                return@count true
            } else {
                if (debug) println("Hailstones' paths will cross outside the test area (at $intersection).")
                return@count false
            }
        }
    }
})

println(startTime.elapsedNow())