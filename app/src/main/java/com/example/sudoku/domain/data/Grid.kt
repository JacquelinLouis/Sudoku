package com.example.sudoku.domain.data

typealias Grid = List<List<Digit>>

fun Grid.toPrettyString() = joinToString(separator = "\n") { row ->
    row.joinToString { digit -> digit.value.toString() }
}