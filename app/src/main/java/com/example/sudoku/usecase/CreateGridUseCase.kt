package com.example.sudoku.usecase

import com.example.sudoku.repository.GridRepository
import java.util.Calendar

class CreateGridUseCase(private val gridRepository: GridRepository) {

    operator fun invoke() = gridRepository.createGrid(
        Calendar.getInstance().time,
        values = VALUES,
        0
    )

    private companion object {
        const val ARRAY_LENGTH = 9
        val VALUES = MutableList(ARRAY_LENGTH * ARRAY_LENGTH) { index ->
            index % ARRAY_LENGTH + 1
        }.run { joinToString(separator = "") }
    }

}