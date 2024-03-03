package com.example.sudoku.domain.usecase

import com.example.sudoku.repository.GridRepository
import java.util.Calendar

class CreateGridUseCase(
    private val gridRepository: GridRepository,
    private val generateGridUseCase: GenerateGridUseCase,
    private val removeDigitsUseCase: RemoveDigitsUseCase
) {

    operator fun invoke(): Long = removeDigitsUseCase(generateGridUseCase(), 9)
        .let { grid ->
        gridRepository.createGrid(
            Calendar.getInstance().time,
            values = grid.joinToString("") { row ->
                row.map { digit ->
                    digit.value
                }.joinToString("")
            },
            grid.joinToString("") { row ->
                row.joinToString("") { digit ->
                    if (digit.fixed) "1" else "0"
                }
            }
        )
    }

}