package com.example.sudoku.domain.usecase

import com.example.sudoku.repository.GridRepository

class DeleteGridUseCase(private val gridRepository: GridRepository) {

    suspend operator fun invoke(gridMetadataId: Long) = gridRepository.deleteGrid(gridMetadataId)

}