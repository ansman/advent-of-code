#!/usr/bin/env kotlin

import kotlin.math.roundToInt
import kotlin.time.TimeSource

val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

val start = TimeSource.Monotonic.markNow()

data class Game(
    val buttonA: Button,
    val buttonB: Button,
    val prize: Prize
) {
    fun solve(): Int {
        val a1 = buttonA.x
        val b1 = buttonB.x
        val c1 = -prize.x
        val a2 = buttonA.y
        val b2 = buttonB.y
        val c2 = -prize.y
        val det1 = a1 * b2 - a2 * b1
        val det2 = a1 * b2 - a2 * b1
        if (det1 == 0 || det2 == 0) {
            return 0
        }

        val num1 = b1 * c2 - b2 * c1
        val num2 = c1 * a2 - c2 * a1

        val a = (num1 / det1.toFloat()).roundToInt()
        val b = (num2 / det2.toFloat()).roundToInt()

        if (a * det1 != num1 || b * det2 != num2) {
            return 0
        }
        return a * 3 + b * 1
    }

    companion object {
        fun read(): Game =
            Game(
                buttonA = Button.parse(readln()),
                buttonB = Button.parse(readln()),
                prize = Prize.parse(readln()),
            )
    }

    data class Button(val x: Int, val y: Int) {
        companion object {
            private val pattern = Regex("""^Button \w: X\+(\d+), Y\+(\d+)$""")
            fun parse(line: String): Button {
                val (x, y) = requireNotNull(pattern.matchEntire(line)?.destructured) {
                    "Line $line didn't match the pattern"
                }
                return Button(x.toInt(), y.toInt())
            }
        }
    }

    data class Prize(val x: Int, val y: Int) {
        companion object {
            private val pattern = Regex("""Prize: X=(\d+), Y=(\d+)""")
            fun parse(line: String): Prize {
                val (x, y) = requireNotNull(pattern.matchEntire(line)?.destructured) {
                    "Line $line didn't match the pattern"
                }
                return Prize(x.toInt(), y.toInt())
            }
        }
    }
}

var tokensSpent = 0
do {
    tokensSpent += Game.read().also { debug(it) }.solve()
} while(readlnOrNull() != null)


println("Tokens spent: $tokensSpent")
println("Ran in ${start.elapsedNow()}")