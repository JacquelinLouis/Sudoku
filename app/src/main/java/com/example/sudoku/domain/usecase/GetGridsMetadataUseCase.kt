package com.example.sudoku.domain.usecase

import com.example.sudoku.repository.GridRepository

class GetGridsMetadataUseCase(private val gridRepository: GridRepository) {

    operator fun invoke() = gridRepository.getGridsMetadata()

}