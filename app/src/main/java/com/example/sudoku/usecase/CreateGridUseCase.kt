package com.example.sudoku.usecase

import com.example.sudoku.repository.GridRepository
import java.util.Calendar

class CreateGridUseCase(private val gridRepository: GridRepository) {

    operator fun invoke() = gridRepository.createGrid(
        Calendar.getInstance().time,
        "",
        0
    )

}