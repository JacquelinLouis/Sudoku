package com.example.sudoku.domain.usecase

import com.example.sudoku.repository.GridRepository
import java.util.Calendar

class CreateGridUseCase(
    private val gridRepository: GridRepository,
    private val generateGridUseCase: GenerateGridUseCase,
    private val removeDigitsUseCase: RemoveDigitsUseCase
) {

    operator fun invoke(): Long = removeDigitsUseCase(generateGridUseCase(), 1)
        .let { grid ->
        gridRepository.createGrid(
            Calendar.getInstance().time,
            grid
        )
    }

}