package com.example.sudoku.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.Config.Companion.GRID_SIZE
import com.example.sudoku.repository.GridRepository
import java.util.Calendar

class CreateGridUseCase(private val gridRepository: GridRepository) {

    operator fun invoke() = gridRepository.createGrid(
        Calendar.getInstance().time,
        values = VALUES,
        0
    )

    private companion object {
        val VALUES = MutableList(GRID_SIZE) { index ->
            index % GRID_LENGTH + 1
        }.run { joinToString(separator = "") }
    }

}