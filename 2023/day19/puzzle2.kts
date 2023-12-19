#!/usr/bin/env kotlin

import kotlin.time.TimeSource

val start = TimeSource.Monotonic.markNow()
val debug = false

enum class Operation(val s: String) {
    LessThan("<"),
    GreaterThan(">"),
}

enum class PartCategory(val s: String) {
    ExtremelyCoolLooking("x"),
    Musical("m"),
    Aerodynamic("a"),
    Shiny("s"),
}

data class Workflow(
        val name: String,
        val rules: List<Rule>,
) {
    companion object {
        private val pattern = Regex("""(\w+)\{(.*)\}""")
        fun parse(input: String): Workflow {
            val (name, rules) = pattern.matchEntire(input)!!.destructured
            return Workflow(name, rules.split(",").map(Rule::parse))
        }
    }

    sealed class Rule {
        companion object {
            private val pattern = Regex("""(\w)([<>])(\d+):(\w+)""")
            fun parse(input: String): Rule {
                val (partCategory, operation, value, outcome) = pattern.matchEntire(input)
                        ?.destructured
                        ?: return Terminal.parse(input)
                return Conditional(
                        partCategory = PartCategory.entries.first { it.s == partCategory },
                        operation = Operation.entries.first { it.s == operation },
                        value = value.toInt(),
                        rule = Terminal.parse(outcome),
                )
            }
        }

        sealed class Terminal : Rule() {
            companion object {
                fun parse(input: String): Terminal =
                        when (input) {
                            "R" -> Rejected
                            "A" -> Accepted
                            else -> ChangeWorkflow(input)
                        }
            }
        }

        data object Accepted : Terminal()
        data object Rejected : Terminal()
        data class ChangeWorkflow(val name: String) : Terminal()
        data class Conditional(
                val partCategory: PartCategory,
                val operation: Operation,
                val value: Int,
                val rule: Terminal,
        ) : Rule()
    }
}

val workflows = generateSequence { readlnOrNull()?.ifEmpty { null } }
        .map(Workflow::parse)
        .associateBy { it.name }

fun computeAccepted(): Long {
    fun Workflow.evaluate(ranges: Map<PartCategory, IntRange>, indent: String = ""): Long {
        if (debug) println("${indent}Evaluating $name with ranges $ranges")
        fun Workflow.Rule.evaluate(ranges: Map<PartCategory, IntRange>): Long {
            return when (this) {
                is Workflow.Rule.Accepted -> ranges.values.fold(1L) { acc, range ->
                    acc * (range.last - range.first + 1)
                }

                is Workflow.Rule.Rejected -> 0L
                is Workflow.Rule.ChangeWorkflow -> workflows.getValue(name).evaluate(ranges, "$indent  ")
                else -> error("Unknown rule: $this")
            }
        }

        var sum = 0L
        val r = ranges.toMutableMap()
        for (rule in rules) {
            when (rule) {
                is Workflow.Rule.Terminal -> sum += rule.evaluate(r)
                is Workflow.Rule.Conditional -> {
                    val range = r[rule.partCategory] ?: 1..4000
                    val (leftRange, rightRange) = when (rule.operation) {
                        Operation.LessThan -> (range.first until rule.value) to (rule.value..range.last)
                        Operation.GreaterThan -> (rule.value + 1..range.last) to (range.first..rule.value)
                    }
                    if (!leftRange.isEmpty()) {
                        sum += rule.rule.evaluate(r + (rule.partCategory to leftRange))
                    }

                    if (rightRange.isEmpty()) {
                        break
                    } else {
                        r[rule.partCategory] = rightRange
                    }
                }

                else -> error("Unknown rule: $rule")
            }
        }
        return sum
    }
    return workflows.getValue("in").evaluate(PartCategory.entries.associateBy({ it }, { 1..4000 }))
}

println(computeAccepted())

println(start.elapsedNow())