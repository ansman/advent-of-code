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

fun Map<PartCategory, Int>.isAccepted(): Boolean {
    fun Workflow.evaluate(): Boolean {
        fun Workflow.Rule.Terminal.isAccepted(): Boolean =
                when (this) {
                    is Workflow.Rule.Accepted -> true
                    is Workflow.Rule.Rejected -> false
                    is Workflow.Rule.ChangeWorkflow -> workflows.getValue(name).evaluate()
                    else -> error("Unknown rule: $this")
                }

        for (rule in rules) {
            when (rule) {
                is Workflow.Rule.Terminal -> return rule.isAccepted()
                is Workflow.Rule.Conditional -> {
                    val value = get(rule.partCategory) ?: 0
                    val matches = when (rule.operation) {
                        Operation.LessThan -> value < rule.value
                        Operation.GreaterThan -> value > rule.value
                    }
                    if (matches) {
                        return rule.rule.isAccepted()
                    }
                }

                else -> error("Unknown rule: $rule")
            }
        }
        error("No rules matched")
    }
    return workflows.getValue("in").evaluate()
}

val parts = generateSequence { readlnOrNull() }
        .map { it.removeSurrounding("{", "}").split(",") }
        .map { categories ->
            categories
                    .map { it.split('=') }
                    .associateBy(
                            { (category, _) -> PartCategory.entries.first { it.s == category } },
                            { (_, value) -> value.toInt() }
                    )
        }
        .filter { it.isAccepted() }
        .sumOf { it.values.sum() }
        .let(::println)

println(start.elapsedNow())