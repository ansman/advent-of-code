#!/usr/bin/env kotlin


import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.TimeSource

val startTime = TimeSource.Monotonic.markNow()
val debug = false
fun debug(msg: Any?) {
    if (debug) {
        println(msg)
    }
}

@Suppress("EnumEntryName")
enum class Instruction(val getOperand: Computer.() -> Int) {
    adv(Computer::getComboOperand) {
        override fun Computer.execute(operand: Int) {
            a = div(a, operand)
            ip += 2
        }
    },

    bxl(Computer::getLiteralOperand) {
        override fun Computer.execute(operand: Int) {
            b = b xor operand
            ip += 2
        }
    },

    bst(Computer::getComboOperand) {
        override fun Computer.execute(operand: Int) {
            b = operand % 8
            ip += 2
        }
    },

    jnz(Computer::getLiteralOperand) {
        override fun Computer.execute(operand: Int) {
            if (a == 0) {
                ip += 2
            } else {
                ip = operand
            }
        }
    },

    bxc(Computer::getLiteralOperand) {
        override fun Computer.execute(operand: Int) {
            b = b xor c
            ip += 2
        }
    },

    `out`(Computer::getComboOperand) {
        override fun Computer.execute(operand: Int) {
            output += operand % 8
            ip += 2
        }
    },

    bdv(Computer::getComboOperand) {
        override fun Computer.execute(operand: Int) {
            b = div(a, operand)
            ip += 2
        }
    },

    cdv(Computer::getComboOperand) {
        override fun Computer.execute(operand: Int) {
            c = div(a, operand)
            ip += 2
        }
    };

    abstract fun Computer.execute(operand: Int)
}

class Computer {
    var a = 0
    var b = 0
    var c = 0
    var ip = 0
    var program = IntArray(0)
    val output = mutableListOf<Int>()

    fun div(num: Int, operand: Int): Int = num / 2.0.pow(operand).roundToInt()

    fun getLiteralOperand(): Int = program[ip + 1]

    fun getComboOperand(): Int =
        when (val v = program[ip + 1]) {
            in 0..3 -> v
            4 -> a
            5 -> b
            6 -> c
            else -> error("Invalid operand $v")
        }

    fun load() {
        a = readln().removePrefix("Register A: ").toInt()
        b = readln().removePrefix("Register B: ").toInt()
        c = readln().removePrefix("Register C: ").toInt()
        readln()
        program = readln().removePrefix("Program: ").split(',').map { it.toInt() }.toIntArray()
    }

    fun run() {
        while (ip in 0..(program.size - 2)) {
            val opCode = program[ip]
            val instruction = Instruction.entries[opCode]
            val operand = instruction.getOperand(this)
            with(instruction) {
                execute(operand)
            }
        }
    }
}

with(Computer()) {
    load()
    run()
    println(output.joinToString(","))
}

println("Ran in ${startTime.elapsedNow()}")