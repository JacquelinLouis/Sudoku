package com.example.sudoku.domain.usecase

import com.example.sudoku.repository.GridRepository
import java.util.Calendar

class CreateGridUseCase(
    private val gridRepository: GridRepository,
    private val generateGridUseCase: GenerateGridUseCase,
    private val removeDigitsUseCase: RemoveDigitsUseCase
) {

    operator fun invoke(): Unit = removeDigitsUseCase(generateGridUseCase(), 1)
        .let { grid ->
            val createdGridId = gridRepository.createGrid(
                Calendar.getInstance().time,
                grid
            )
            gridRepository.setCurrentGridId(createdGridId)
    }

}