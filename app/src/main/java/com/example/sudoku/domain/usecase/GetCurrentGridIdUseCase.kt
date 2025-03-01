package com.example.sudoku.domain.usecase

import com.example.sudoku.repository.GridRepository

class GetCurrentGridIdUseCase(private val gridRepository: GridRepository) {

    operator fun invoke() = gridRepository.currentGrid

}