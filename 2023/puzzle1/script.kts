#!/usr/bin/env kotlin

generateSequence { readlnOrNull() }
        .map { line -> line.filter { it in '0'..'9' } }
        .map { "${it.first()}${it.last()}" }
        .map { it.toInt() }
        .sum()
        .let { println(it) }