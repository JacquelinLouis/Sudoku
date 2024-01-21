package com.example.sudoku.repository

import com.example.sudoku.repository.source.room.GridDao
import com.example.sudoku.repository.source.room.GridDataEntity
import com.example.sudoku.repository.source.room.GridMetadataEntity
import io.mockk.coEvery
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

    @Test
    fun testCreateGridInsertGridMetadataEntity() {
        val date = Date(0)

        gridRepository.createGrid(date, "1234", 0)

        verify { gridDao.insert(GridMetadataEntity(0L, date)) }
    }

    @Test
    fun testCreateGridInsertGridDataEntity() {
        val date = Date(0)
        val values = "1234"
        val fixedValues = 0
        val gridMetadataId = 1L
        every { gridDao.insert(GridMetadataEntity(creation = date)) }.returns(gridMetadataId)

        gridRepository.createGrid(Date(0), values, fixedValues)

        verify { gridDao.insert(GridDataEntity(0L, gridMetadataId, values, fixedValues)) }
    }

    @Test
    fun testCreateGridReturnGridMetadataId() {
        val date = Date(0)
        val values = "1234"
        val fixedValues = 0
        val gridMetadataId = 1L
        every { gridDao.insert(GridMetadataEntity(creation = date)) }.returns(gridMetadataId)

        val result = gridRepository.createGrid(Date(0), values, fixedValues)

        assertEquals(gridMetadataId, result)
    }

    @Test
    fun testGetGridsMetadata() {
        val gridsMetadata = flowOf(listOf(GridMetadataEntity(creation = Date(0))))
        every { gridDao.get() }.returns(gridsMetadata)

        val result = gridRepository.getGridsMetadata()

        assertEquals(gridsMetadata, result)
    }

    @Test
    fun testGetGridData() {
        val gridMetadataId = 3L
        val gridsData = flowOf(GridDataEntity(0L, gridMetadataId, "1234", 1))
        every { gridDao.get(gridMetadataId) }.returns(gridsData)

        val result = gridRepository.getGridData(gridMetadataId)

        assertEquals(gridsData, result)

    }

    @Test
    fun testUpdateGrid() {
        val gridMetadataId = 3L
        val gridDataEntity = GridDataEntity(0L, gridMetadataId, "1234", 1)
        val updatedGridDataEntity = gridDataEntity.copy(values = "5678")
        every { gridDao.get(gridMetadataId) }.returns(flowOf(gridDataEntity))

        runBlocking { gridRepository.updateGrid(gridMetadataId, updatedGridDataEntity.values) }

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