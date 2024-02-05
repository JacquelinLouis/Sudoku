package com.example.sudoku.extension

fun <T> List<T>.contains(block: (T) -> Boolean) = find(block) != null