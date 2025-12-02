#!/usr/bin/env kotlin

val number = generateSequence { readlnOrNull() }
    .flatMap { it.split(',') }
    .map { r -> r.split('-').map { it.toLong() } }
    .map { (f, t) -> f..t }
    .flatMap { it }
    .map { it.toString() }
    .filter { it.length % 2 == 0 }
    .filter { it.take(it.length / 2) == it.drop(it.length / 2) }
    .sumOf { it.toLong() }

println(number)