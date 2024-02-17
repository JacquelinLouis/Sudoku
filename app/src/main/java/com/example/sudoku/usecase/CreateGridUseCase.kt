package com.example.sudoku.usecase

import com.example.sudoku.repository.GridRepository
import java.util.Calendar

class CreateGridUseCase(
    private val gridRepository: GridRepository,
    private val generateGridUseCase: GenerateGridUseCase
) {

    operator fun invoke(): Long = generateGridUseCase().let { grid ->
        gridRepository.createGrid(
            Calendar.getInstance().time,
            values = grid.joinToString("") { row ->
                row.map { digit ->
                    digit.value
                }.joinToString("")
            },
            0
        )
    }

}