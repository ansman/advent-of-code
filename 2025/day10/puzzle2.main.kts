#!/usr/bin/env kotlin

import kotlin.math.abs
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()

data class Computer(
    val lights: List<Boolean>,
    val buttons: List<Set<Int>>,
    val joltage: List<Int>
) {
    companion object {
        private val pattern = """\[(.+)\] (.+) \{(.+)\}""".toRegex()

        fun parse(input: String): Computer {
            val (lights, buttons, joltage) = pattern.matchEntire(input)!!.destructured
            return Computer(
                lights = lights.map { it == '#' },
                buttons = buttons.split(" ").map { button ->
                    button.removeSurrounding("(", ")")
                        .split(",")
                        .map(String::toInt)
                        .toSet()
                },
                joltage = joltage.split(",").map(String::toInt)
            )
        }
    }
}

private data class Fraction(val n: Long, val d: Long) {
    init {
        require(d != 0L)
    }

    fun simplify(): Fraction {
        val g = gcd(abs(n), abs(d))
        val nn = n / g
        val dd = d / g
        return if (dd < 0) Fraction(-nn, -dd) else Fraction(nn, dd)
    }

    operator fun plus(o: Fraction) = Fraction(n * o.d + o.n * d, d * o.d).simplify()
    operator fun minus(o: Fraction) = Fraction(n * o.d - o.n * d, d * o.d).simplify()
    operator fun times(o: Fraction) = Fraction(n * o.n, d * o.d).simplify()
    operator fun div(o: Fraction) = Fraction(n * o.d, d * o.n).simplify()
}

private fun gcd(a: Long, b: Long): Long =
    if (b == 0L) a else gcd(b, a % b)

private fun Computer.solve(): Long {
    val m = joltage.size
    val n = buttons.size

    val mat = Array(m) { r ->
        Array(n + 1) { c ->
            if (c == n) {
                Fraction(joltage[r].toLong(), 1)
            } else {
                Fraction(0, 1)
            }
        }
    }
    for (j in 0 until n) {
        for (i in buttons[j]) {
            mat[i][j] = Fraction(1, 1)
        }
    }

    val pivotCols = mutableListOf<Int>()
    var row = 0
    for (col in 0 until n) {
        var pivot = -1
        for (r in row until m) {
            if (mat[r][col].n != 0L) {
                pivot = r
                break
            }
        }
        if (pivot == -1) {
            continue
        }

        mat[row] = mat[pivot].also { mat[pivot] = mat[row] }

        val inv = Fraction(1, 1) / mat[row][col]
        for (c in col until n + 1) {
            mat[row][c] = mat[row][c] * inv
        }

        for (r in 0 until m) {
            if (r == row || mat[r][col].n == 0L) continue
            val factor = mat[r][col]
            for (c in col until n + 1) {
                mat[r][c] = mat[r][c] - mat[row][c] * factor
            }
        }

        pivotCols += col
        row++
        if (row == m) {
            break
        }
    }

    for (r in 0 until m) {
        val allZero = (0 until n).all { mat[r][it].n == 0L }
        if (allZero && mat[r][n].n != 0L) {
            error("No solution for machine")
        }
    }

    val freeCols = (0 until n).filter { it !in pivotCols }
    val k = freeCols.size

    val pivotRows = pivotCols.size
    val rhs = Array(pivotRows) { mat[it][n] }
    val coeff = Array(pivotRows) { r -> Array(k) { idx -> mat[r][freeCols[idx]] } }

    val bounds = IntArray(n) { j -> buttons[j].minOf { joltage[it] } }

    var best = Long.MAX_VALUE
    val freeVals = IntArray(k)

    fun dfs(idx: Int) {
        if (idx == k) {
            val x = LongArray(n) { 0L }
            for (f in 0 until k) {
                x[freeCols[f]] = freeVals[f].toLong()
            }
            for (r in 0 until pivotRows) {
                var value = rhs[r]
                for (f in 0 until k) {
                    value -= coeff[r][f] * Fraction(freeVals[f].toLong(), 1)
                }
                // value should be integer
                if (value.d != 1L) return
                val v = value.n
                val col = pivotCols[r]
                if (v < 0 || v > bounds[col]) return
                x[col] = v
            }
            val cost = x.sum()
            if (cost < best) best = cost
            return
        }

        val col = freeCols[idx]
        val maxV = bounds[col]
        val currentSum = freeVals.take(idx).sumOf { it.toLong() }
        if (best != Long.MAX_VALUE && currentSum >= best) return

        for (v in 0..maxV) {
            freeVals[idx] = v
            dfs(idx + 1)
        }
    }

    dfs(0)
    check(best != Long.MAX_VALUE) { "No solution found for machine" }
    return best
}

val answer = generateSequence { readlnOrNull() }
    .map { Computer.parse(it) }
    .map { it.solve() }
    .sum()

println(answer)
println("Ran in ${startTime.elapsedNow().inWholeMilliseconds}ms")
