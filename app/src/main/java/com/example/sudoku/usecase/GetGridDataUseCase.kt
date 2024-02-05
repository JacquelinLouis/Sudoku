package com.example.sudoku.usecase

import com.example.sudoku.repository.GridRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetGridDataUseCase(private val gridRepository: GridRepository) {

    operator fun invoke(gridMetadataId: Long): Flow<Grid> = gridRepository
        .getGridData(gridMetadataId)
        .map { gridDataEntity ->
            mutableListOf<List<Pair<Short, Boolean>>>().apply {
                for (i in 0 until ARRAY_LENGTH) {
                    val row = gridDataEntity.values.substring(i * ARRAY_LENGTH until  (i + 1) * ARRAY_LENGTH)
                    add(row.map { Pair(it.toString().toShort(), false) })
                }
            }
        }

    private companion object {
        const val ARRAY_LENGTH = 9
    }
}