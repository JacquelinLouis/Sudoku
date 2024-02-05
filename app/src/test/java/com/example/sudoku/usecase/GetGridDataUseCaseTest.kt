package com.example.sudoku.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.repository.GridRepository
import com.example.sudoku.repository.source.room.GridDataEntity
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetGridDataUseCaseTest {

    private val gridMetadataId = 0L

    private val values = MutableList<List<Int>>(GRID_LENGTH) { _ ->
        MutableList(GRID_LENGTH) { x -> x }
    }

    private val expected = values.map { row -> row.map { Pair(it.toShort(), false) } }

    private val gridDataEntity = GridDataEntity(
        gridMetadataId = 0L,
        values = values.joinToString("") { it.joinToString("") },
        fixedValues = 0
    )

    private val gridRepository = mockk<GridRepository> {
        every { getGridData(gridMetadataId) }.returns(flowOf(gridDataEntity))
    }

    private val getGridDataUseCase = GetGridDataUseCase(gridRepository)

    @Test
    fun testInvoke() {
        val result = runBlocking { getGridDataUseCase(gridMetadataId).first() }

        assertEquals(expected, result)
    }

}