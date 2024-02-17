package com.example.sudoku.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.repository.GridRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetGridDataUseCase(private val gridRepository: GridRepository) {

    operator fun invoke(gridMetadataId: Long): Flow<Grid> = gridRepository
        .getGridData(gridMetadataId)
        .map { gridDataEntity ->
            mutableListOf<List<Digit>>().apply {
                for (i in 0 until GRID_LENGTH) {
                    val row = gridDataEntity.values.substring(i * GRID_LENGTH until  (i + 1) * GRID_LENGTH)
                    add(row.map { Digit(it.toString().toShort(), false) })
                }
            }
        }

}