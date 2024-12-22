#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

class Sequence private constructor(private val sequence: ByteArray) {
    constructor() : this(ByteArray(size))
    constructor(b1: Byte, b2: Byte, b3: Byte, b4: Byte) : this(byteArrayOf(b1, b2, b3, b4))

    override fun hashCode(): Int = sequence.contentHashCode()
    override fun equals(other: Any?): Boolean =
        other === this || other is Sequence && sequence.contentEquals(other.sequence)

    operator fun plus(number: Byte): Sequence {
        val newSequence = ByteArray(size)
        sequence.copyInto(newSequence, startIndex = 1)
        newSequence[sequence.lastIndex] = number
        return Sequence(newSequence)
    }

    override fun toString(): String = sequence.joinToString(",")

    companion object {
        const val size = 4
    }
}

class Bananas {
    var count: Long = 0
        private set

    private val seenNumbers = mutableSetOf<Int>()
    private val bananas = mutableListOf<Byte>()

    fun addForNumber(number: Int, bananas: Byte) {
        if (seenNumbers.add(number)) {
            count += bananas
            this.bananas += bananas
        }
    }

    override fun toString(): String = count.toString()
}

val bananasPerSequence = mutableMapOf<Sequence, Bananas>()

fun runIteration(number: Long): Long {
    var num = number
    num = (num xor (num shl 6)) % 16777216
    num = (num xor (num ushr 5)) % 16777216
    num = (num xor (num shl 11)) % 16777216
    return num
}

fun computeSequences(id: Int, number: Long, numbers: Int) {
    var num = number
    var prev = (number % 10).toByte()
    var sequence = Sequence()
    repeat(numbers - 1) {
        num = runIteration(num)
        val bananas = (num % 10).toByte()
        sequence += (bananas - prev).toByte()
        if (it >= Sequence.size - 1) {
            bananasPerSequence.getOrPut(sequence, ::Bananas).addForNumber(id, bananas)
        }
        prev = bananas
    }
}

generateSequence { readlnOrNull() }
    .forEachIndexed { i, num -> computeSequences(i, num.toLong(), 2000) }

val mostBananas = bananasPerSequence.entries.maxBy { it.value.count }
println(mostBananas.value)

println("Ran in ${startTime.elapsedNow()}")