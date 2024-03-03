package com.example.sudoku.domain.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.domain.data.Digit
import com.example.sudoku.domain.data.GridData
import com.example.sudoku.repository.GridRepository
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

    private val expected = values.map { row -> row.map { Digit(it, false) } }

    private val gridData = GridData(
        id = 0L,
        digits = expected
    )

    private val gridRepository = mockk<GridRepository> {
        every { getGridData(gridMetadataId) }.returns(flowOf(gridData))
    }

    private val getGridDataUseCase = GetGridDataUseCase(gridRepository)

    @Test
    fun testInvoke() {
        val result = runBlocking { getGridDataUseCase(gridMetadataId).first() }

        assertEquals(expected, result)
    }

}