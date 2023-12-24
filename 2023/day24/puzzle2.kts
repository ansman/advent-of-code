#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = args.isNotEmpty()

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
    fun timeAtX(x: Double): Double = (x - position.x) / velocity.x
    fun timeAtY(y: Double): Double = (y - position.y) / velocity.y

    override fun toString(): String = "$position @ $velocity"

    fun hits(other: Hailstone): Boolean {
        // Computes time of intersection for one of the axis
        val t = when {
            velocity.x != other.velocity.x -> (other.position.x - position.x) / (velocity.x - other.velocity.x)
            velocity.y != other.velocity.y -> (other.position.y - position.y) / (velocity.y - other.velocity.y)
            velocity.z != other.velocity.z -> (other.position.z - position.z) / (velocity.z - other.velocity.z)
            else -> return position == other.position
        }
        // Verify that all other axis match
        return other.position.x + t * other.velocity.x == position.x + t * velocity.x &&
                other.position.y + t * other.velocity.y == position.y + t * velocity.y &&
                other.position.z + t * other.velocity.z == position.z + t * velocity.z
    }

    companion object {
        fun parse(input: String): Hailstone {
            val (position, velocity) = input.split(" @ ").map { XYZ.parse(it) }
            return Hailstone(position, velocity)

        }
    }
}

val velocityRange = (args.getOrNull(0)?.toLong() ?: 500).let { -it..it }.asSequence()

val hailstones = generateSequence(::readlnOrNull)
        .map { Hailstone.parse(it) }
        .toList()

val h1 = hailstones[0]
val h2 = hailstones[1]

fun computeRock(
        h1: Hailstone,
        h2: Hailstone,
        vx: Long,
        vy: Long,
        vz: Long,
): Hailstone? {
    // No velocity, cannot hit
    if (vx == 0L && vy == 0L && vz == 0L) {
        return null
    }
    val xm1 = h1.position.x
    val xk1 = h1.velocity.x - vx
    val ym1 = h1.position.y
    val yk1 = h1.velocity.y - vy
    val xm2 = h2.position.x
    val xk2 = h2.velocity.x - vx
    val ym2 = h2.position.y
    val yk2 = h2.velocity.y - vy

    // Parallel, cannot hit
    if (xk2 == 0L || (xk1 * yk2) - (yk1 * xk2) == 0L) {
        return null
    }

    // Compute time of intersection
    val t = (yk2 * (xm2 - xm1) - xk2 * (ym2 - ym1)) / ((xk1 * yk2) - (yk1 * xk2))

    // Computes initial position, given time of intersection
    val x = h1.position.x + h1.velocity.x * t - vx * t
    val y = h1.position.y + h1.velocity.y * t - vy * t
    val z = h1.position.z + h1.velocity.z * t - vz * t

    return Hailstone(
            position = XYZ(x, y, z),
            velocity = XYZ(vx, vy, vz)
    )
}

velocityRange
        .flatMap { vx ->
            velocityRange.flatMap { vy ->
                velocityRange.mapNotNull { vz ->
                    computeRock(h1, h2, vx, vy, vz)
                }
            }
        }
        .first { rock -> hailstones.all { h -> rock.hits(h) } }
        .let { rock -> println(rock.position.x + rock.position.y + rock.position.z) }

println(startTime.elapsedNow())