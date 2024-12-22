#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

class Sequence private constructor(
    private val b1: Byte,
    private val b2: Byte,
    private val b3: Byte,
    private val b4: Byte
) {
    val id = (b1 + 9) * 19 * 19 * 19 + (b2 + 9) * 19 * 19 + (b3 + 9) * 19 + (b4 + 9)
    constructor() : this(0, 0, 0, 0)

    override fun hashCode(): Int = id
    override fun equals(other: Any?): Boolean =
        other === this || other is Sequence && id == other.id

    operator fun plus(number: Byte): Sequence = Sequence(b2, b3, b4, number)

    override fun toString(): String = "$b1,$b2,$b3,$b4"
}

class Bananas {
    var count: Long = 0
        private set

    private val seenNumbers = BooleanArray(2500)

    fun addForNumber(number: Int, bananas: Long) {
        if (!seenNumbers[number]) {
            count += bananas
            seenNumbers[number] = true
        }
    }

    override fun toString(): String = count.toString()
}

val bananasPerSequence = HashMap<Sequence, Bananas>(50000)

fun runIteration(number: Long): Long {
    var num = number
    num = (num xor (num shl 6)) % 16777216
    num = (num xor (num ushr 5))
    num = (num xor (num shl 11)) % 16777216
    return num
}

fun computeSequences(id: Int, number: Long, numbers: Int) {
    var num = number
    var prev = number % 10
    var sequence = Sequence()
    repeat(numbers - 1) {
        num = runIteration(num)
        val bananas = num % 10
        sequence += (bananas - prev).toByte()
        if (it >= 3) {
            bananasPerSequence.getOrPut(sequence, ::Bananas).addForNumber(id, bananas)
        }
        prev = bananas
    }
}

generateSequence { readlnOrNull() }
    .forEachIndexed { i, num -> computeSequences(i, num.toLong(), 2000) }

val mostBananas = bananasPerSequence.values.maxBy { it.count }
println(mostBananas)

println("Ran in ${startTime.elapsedNow()}")