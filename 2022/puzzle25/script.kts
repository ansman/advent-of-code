#!/usr/bin/env kotlin

import java.text.ParsePosition

sealed class Packet {
    data class Single(val value: Int) : Packet() {
        override fun toString(): String = value.toString()
    }

    data class Multiple(val values: List<Packet>) : Packet() {
        override fun toString(): String = values.joinToString(",", prefix = "[", postfix = "]")
    }
}

fun String.readAndConsume(position: ParsePosition = ParsePosition(0)): Packet =
        if (get(position.index) == '[') {
            ++position.index
            Packet.Multiple(buildList {
                while (this@readAndConsume.get(position.index) != ']') {
                    add(readAndConsume(position))
                }
                // Skips ]
                ++position.index
                if (this@readAndConsume.getOrNull(position.index) == ',') ++position.index
            })
        } else {
            val start = position.index
            while (position.index < length && get(position.index).isDigit()) {
                ++position.index
            }
            val end = position.index
            if (get(position.index) == ',') ++position.index
            Packet.Single(substring(start, end).toInt())
        }

fun isInOrder(left: Packet, right: Packet, indent: String = ""): Boolean? {
    println("$indent- Compare $left and $right")
    return when (left) {
        is Packet.Single ->
            when (right) {
                is Packet.Single -> {
                    when {
                        left.value < right.value -> {
                            println("$indent- Left side is smaller, so inputs are in the right order")
                            true
                        }

                        left.value > right.value -> {
                            println("$indent- Right side is smaller, so inputs are not in the right order")
                            false
                        }

                        else -> null
                    }
                }
                is Packet.Multiple -> isInOrder(Packet.Multiple(listOf(left)), right, indent + "  ")
            }

        is Packet.Multiple ->
            when (right) {
                is Packet.Single -> isInOrder(left, Packet.Multiple(listOf(right)), indent + "  ")

                is Packet.Multiple -> {
                    var i = 0
                    while (i < left.values.size || i < right.values.size) {
                        val l = left.values.getOrNull(i) ?: run {
                            println("$indent- Left side ran out of items, so inputs are in the right order")
                            return true
                        }
                        val r = right.values.getOrNull(i) ?: run {
                            println("$indent- Right side ran out of items, so inputs are not in the right order")
                            return false
                        }
                        isInOrder(l, r, indent + "  ")?.let { return it }
                        ++i
                    }
                    null
                }
            }
    }
}

var i = 1
var sum = 0
while (true) {
    val left = readLine()?.readAndConsume() ?: break
    val right = readLine()!!.readAndConsume()
    readLine() // Empty line
    println(left)
    println(right)
    if (isInOrder(left, right)!!) {
        sum += i
    }
    println()
    ++i
}
println(sum)