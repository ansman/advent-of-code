#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.collections.LinkedHashMap
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun String.hash(): Int = fold(0) { v, c -> (v + c.code) * 17 % 256 }

val operationPattern = Regex("""(\w+)(-|=)(\d+)?""")
fun Array<LinkedHashMap<String, Int>>.applyOperation(operation: String) = apply {
    val (label, op, focalLength) = operationPattern.matchEntire(operation)!!.destructured
    val box = this[label.hash()]
    when (op) {
        "-" -> box.remove(label)
        "=" -> box[label] = focalLength.toInt()
        else -> error("Unknown operation $op")
    }
}

generateSequence(::readlnOrNull)
        .flatMap { it.split(",") }
        .fold(Array<LinkedHashMap<String, Int>>(256, ::LinkedHashMap)) { map, op ->
            map.applyOperation(op)
        }
        .foldIndexed(0) { boxIndex, sum, box ->
            box.values.foldIndexed(sum) { slot, acc, focalLength ->
                acc + (boxIndex + 1) * (slot + 1) * focalLength
            }
        }
        .let(::println)

println(start.elapsedNow())