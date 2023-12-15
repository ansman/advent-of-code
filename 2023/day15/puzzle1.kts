#!/usr/bin/env kotlin

import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.*
import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

fun String.hash(): Int = fold(0) { v, c -> (v + c.code) * 17 % 256 }

generateSequence(::readlnOrNull)
        .flatMap { it.split(",") }
        .map { it.hash() }
        .sum()
        .let(::println)

println(start.elapsedNow())