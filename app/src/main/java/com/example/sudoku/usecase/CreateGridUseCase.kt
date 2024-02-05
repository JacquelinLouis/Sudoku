package com.example.sudoku.usecase

import com.example.sudoku.Config.Companion.INVALID_GRID_ID
import com.example.sudoku.repository.GridRepository
import java.util.Calendar

class CreateGridUseCase(
    private val gridRepository: GridRepository,
    private val isValidGridUseCase: IsValidGridUseCase
) {

    operator fun invoke(): Long = VALUES.let { values ->
        val grid = values.map { row -> row.map { Pair(it, false) } }
        if (isValidGridUseCase(grid))
            gridRepository.createGrid(
                Calendar.getInstance().time,
                values = values.joinToString("") { it.joinToString("") },
                0
            )
        else
            INVALID_GRID_ID
    }

    private companion object {
        val VALUES = listOf(
            listOf<Short>(1, 2, 3, 4, 5, 6, 7, 8, 9),
            listOf<Short>(7, 8, 9, 1, 2, 3, 4, 5, 6),
            listOf<Short>(4, 5, 6, 7, 8, 9, 1, 2, 3),

            listOf<Short>(9, 1, 2, 3, 4, 5, 6, 7, 8),
            listOf<Short>(6, 7, 8, 9, 1, 2, 3, 4, 5),
            listOf<Short>(3, 4, 5, 6, 7, 8, 9, 1, 2),

            listOf<Short>(8, 9, 1, 2, 3, 4, 5, 6, 7),
            listOf<Short>(5, 6, 7, 8, 9, 1, 2, 3, 4),
            listOf<Short>(2, 3, 4, 5, 6, 7, 8, 9, 1)
        )
    }

}