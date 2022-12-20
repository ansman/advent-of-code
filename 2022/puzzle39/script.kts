#!/usr/bin/env kotlin

import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.script.dependencies.ScriptContents
import kotlin.time.Duration.Companion.nanoseconds

val start = System.nanoTime()
Runtime.getRuntime().addShutdownHook(Thread {
    println("Total runtime ${(System.nanoTime() - start).nanoseconds}")
})

val logging = false

fun log(msg: Any?) {
    if (logging) {
        println(msg)
    }
}

val numbers = buildList {
    while (true) {
        add(readLine()?.toInt() ?: break)
    }
}.withIndex().toList().toTypedArray()

fun <E> Array<E>.moveNumberUp(fromIndex: Int, toIndex: Int) {
    require(fromIndex < toIndex)
    val target = this[fromIndex]
    for (i in fromIndex until toIndex) {
        this[i] = this[i + 1]
    }
    this[toIndex] = target
}

fun <E> Array<E>.moveNumberDown(fromIndex: Int, toIndex: Int) {
    require(fromIndex > toIndex)
    val target = this[fromIndex]
    for (i in fromIndex downTo  toIndex + 1) {
        this[i] = this[i - 1]
    }
    this[toIndex] = target
}

fun printList() {
    if (logging) {
        log(numbers.joinToString(", ") { it.value.toString() })
        log("")
    }
}

var offset = 0
printList()
repeat(numbers.size) { n ->
    log("Processing number $n")
    var i = n + offset
    while (numbers[i].index != n) {
        ++i
        ++offset
    }
    val number = numbers[i]
    var newIndex = i + number.value
    if (newIndex <= 0) {
        while (newIndex <= 0) {
            newIndex += numbers.size - 1
        }
    } else if (newIndex >= numbers.size) {
        while (newIndex >= numbers.size) {
            newIndex -= numbers.size - 1
        }
    }

    if (newIndex < i) {
        // We're moving backwards
        log("Moving number ${numbers[i].value} down from $i to $newIndex, offset=$offset")
        numbers.moveNumberDown(i, newIndex)
    } else if (newIndex > i) {
        --offset
        log("Moving number ${numbers[i].value} up from $i to $newIndex, offset=$offset")
        numbers.moveNumberUp(i, newIndex)
    }
    printList()
}

val zero = numbers.indexOfFirst { it.value == 0 }
println(sequenceOf(1000, 2000, 3000)
        .map { numbers[(zero + it) % numbers.size].value }
        .sum())