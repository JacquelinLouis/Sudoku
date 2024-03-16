package com.example.sudoku.repository

import com.example.sudoku.Config
import com.example.sudoku.domain.data.Digit
import com.example.sudoku.domain.data.Grid
import com.example.sudoku.domain.data.GridData
import com.example.sudoku.domain.data.GridMetadata
import com.example.sudoku.repository.source.room.GridDao
import com.example.sudoku.repository.source.room.GridDataEntity
import com.example.sudoku.repository.source.room.GridMetadataEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Date

class GridRepository(
    private val gridDao: GridDao
) {

    fun createGrid(date: Date, digits: List<List<Digit>>): Long {
        val gridMetadataId = gridDao.insert(GridMetadataEntity(creation = date))

        val values = digits.joinToString("") { row ->
            row.map { digit ->
                digit.value
            }.joinToString("")
        }

        val fixedValues = digits.joinToString("") { row ->
            row.joinToString("") { digit ->
                if (digit.fixed) "1" else "0"
            }
        }

        gridDao.insert(GridDataEntity(
            gridMetadataId = gridMetadataId,
            values = values,
            fixedValues = fixedValues
        ))
        return gridMetadataId
    }

    fun getGridsMetadata() = gridDao.get().map { gridsMetadata ->
        gridsMetadata.map { GridMetadata(it.gridMetadataId, it.creation) }
    }

    fun getGridData(gridMetadataId: Long) = gridDao.get(gridMetadataId).map { gridDataEntity ->
        val digits = mutableListOf<List<Digit>>().apply {
            for (i in 0 until Config.GRID_LENGTH) {
                val values = gridDataEntity.values.substring(i * Config.GRID_LENGTH until  (i + 1) * Config.GRID_LENGTH).map { it.toString().toInt() }
                val fixed = gridDataEntity.fixedValues.substring(i * Config.GRID_LENGTH until  (i + 1) * Config.GRID_LENGTH).map { it == '1' }
                add(values.mapIndexed { index, value ->
                    Digit(
                        value,
                        fixed[index]
                    )
                })
            }
        }

        GridData(
            gridDataEntity.gridDataId,
            digits
        )
    }

    suspend fun updateGrid(gridMetadataId: Long, grid: Grid) {
        val values = grid.joinToString("") { row ->
            row.joinToString("") { it.value.toString() }
        }
        gridDao.update(gridDao.get(gridMetadataId).first().copy(values = values))
    }

    suspend fun deleteGrid(gridMetadataId: Long) {
        gridDao.delete(
            gridDao.get().first().first { it.gridMetadataId == gridMetadataId }
        )
    }
}