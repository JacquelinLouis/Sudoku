package com.example.sudoku.domain.data

data class GridData(
    val id: Long,
    val digits: List<List<Digit>>,
)