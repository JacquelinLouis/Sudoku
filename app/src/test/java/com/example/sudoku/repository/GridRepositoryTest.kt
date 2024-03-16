package com.example.sudoku.repository

import com.example.sudoku.domain.data.Digit
import com.example.sudoku.domain.data.GridData
import com.example.sudoku.domain.data.GridMetadata
import com.example.sudoku.repository.source.room.GridDao
import com.example.sudoku.repository.source.room.GridDataEntity
import com.example.sudoku.repository.source.room.GridMetadataEntity
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class GridRepositoryTest {

    private val gridDao = mockk<GridDao>(relaxed = true)

    private val gridRepository = GridRepository(gridDao)

    private val digits = MutableList(9) { _ ->
        MutableList(9) { columnIndex -> Digit(columnIndex, columnIndex % 3 == 0) }
    }

    private val values = digits.joinToString("") { row ->
        row.joinToString("") { digit ->
            digit.value.toString()
        }
    }

    private val fixedValues = digits.joinToString("") { row ->
        row.joinToString("") { digit ->
            if (digit.fixed) "1" else "0"
        }
    }

    @Test
    fun testCreateGridInsertGridMetadataEntity() {
        val date = Date(0)

        gridRepository.createGrid(date, digits)

        verify { gridDao.insert(GridMetadataEntity(0L, date)) }
    }

    @Test
    fun testCreateGridInsertGridDataEntity() {
        val date = Date(0)
        val gridMetadataId = 1L
        every { gridDao.insert(GridMetadataEntity(creation = date)) }.returns(gridMetadataId)

        gridRepository.createGrid(Date(0), digits)

        verify { gridDao.insert(GridDataEntity(0L, gridMetadataId, values, fixedValues)) }
    }

    @Test
    fun testCreateGridReturnGridMetadataId() {
        val date = Date(0)
        val gridMetadataId = 1L
        every { gridDao.insert(GridMetadataEntity(creation = date)) }.returns(gridMetadataId)

        val result = gridRepository.createGrid(Date(0), digits)

        assertEquals(gridMetadataId, result)
    }

    @Test
    fun testGetGridsMetadata() {
        val gridsMetadata = flowOf(listOf(GridMetadataEntity(creation = Date(0))))
        every { gridDao.get() }.returns(gridsMetadata)
        val expected = listOf(GridMetadata(0, Date(0)))

        val result = runBlocking { gridRepository.getGridsMetadata().first() }

        assertEquals(expected, result)
    }

    @Test
    fun testGetGridData() {
        val gridMetadataId = 3L
        val gridDataEntity = GridDataEntity(0L, gridMetadataId, values, fixedValues)
        val gridsDataFlow = flowOf(gridDataEntity)
        val expected = GridData(
            id = gridDataEntity.gridDataId,
            digits = digits
        )
        every { gridDao.get(gridMetadataId) }.returns(gridsDataFlow)

        val result = runBlocking { gridRepository.getGridData(gridMetadataId).first() }

        assertEquals(expected, result)

    }

    @Test
    fun testUpdateGrid() {
        val gridDataEntity = GridDataEntity(0L, 3L, values, fixedValues)
        val updatedGrid = digits.map { row ->
            row.map { digit ->
                Digit(digit.value.takeIf { it != 1 } ?: 2, digit.fixed)
            }
        }
        val updatedGridDataEntity = gridDataEntity.copy(values = values.replace("1", "2"))
        every { gridDao.get(gridDataEntity.gridDataId) }.returns(flowOf(gridDataEntity))

        runBlocking { gridRepository.updateGrid(gridDataEntity.gridDataId, updatedGrid) }

        coVerify { gridDao.update(updatedGridDataEntity) }
    }

    @Test
    fun testDeleteGrid() {
        val gridMetadataId = 3L
        val gridMetadataEntity = GridMetadataEntity(gridMetadataId, Date(0))
        every { gridDao.get() }.returns(flowOf(listOf(gridMetadataEntity)))

        runBlocking { gridRepository.deleteGrid(gridMetadataId) }

        coVerify { gridDao.delete(gridMetadataEntity) }
    }
}