package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.Grid
import com.example.sudoku.repository.GridRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetGridDataUseCase(private val gridRepository: GridRepository) {

    operator fun invoke(gridMetadataId: Long): Flow<Grid?> = gridRepository
        .getGridData(gridMetadataId).map { it?.digits }

}