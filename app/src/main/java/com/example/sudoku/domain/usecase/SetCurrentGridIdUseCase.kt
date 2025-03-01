package com.example.sudoku.domain.usecase

import com.example.sudoku.repository.GridRepository

class SetCurrentGridIdUseCase(private val gridRepository: GridRepository) {

    operator fun invoke(id: Long) = gridRepository.setCurrentGridId(id)

}