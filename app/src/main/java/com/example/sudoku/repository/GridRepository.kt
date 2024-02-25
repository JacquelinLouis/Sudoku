package com.example.sudoku.repository

import com.example.sudoku.repository.source.room.GridDao
import com.example.sudoku.repository.source.room.GridDataEntity
import com.example.sudoku.repository.source.room.GridMetadataEntity
import kotlinx.coroutines.flow.first
import java.util.Date

class GridRepository(
    private val gridDao: GridDao
) {

    fun createGrid(date: Date, values: String, fixedValues: String): Long {
        val gridMetadataId = gridDao.insert(GridMetadataEntity(creation = date))
        gridDao.insert(GridDataEntity(
            gridMetadataId = gridMetadataId,
            values = values,
            fixedValues = fixedValues
        ))
        return gridMetadataId
    }

    fun getGridsMetadata() = gridDao.get()

    fun getGridData(gridMetadataId: Long) = gridDao.get(gridMetadataId)

    suspend fun updateGrid(gridMetadataId: Long, values: String) {
        gridDao.update(getGridData(gridMetadataId).first().copy(values = values))
    }

    suspend fun deleteGrid(gridMetadataId: Long) {
        gridDao.delete(getGridsMetadata().first().first { it.gridMetadataId == gridMetadataId })
    }
}