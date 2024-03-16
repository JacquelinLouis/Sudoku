package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.Grid
import com.example.sudoku.repository.GridRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UpdateGridDataUseCase(private val gridRepository: GridRepository) {

    suspend operator fun invoke(gridMetadataId: Long, grid: Grid) = withContext(Dispatchers.IO) {
        gridRepository.updateGrid(gridMetadataId, grid)
    }

}