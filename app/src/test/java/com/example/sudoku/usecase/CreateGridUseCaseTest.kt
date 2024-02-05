package com.example.sudoku.usecase

import com.example.sudoku.repository.GridRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.Date

class CreateGridUseCaseTest {

    private val date = Date(0)

    private val gridValues = listOf(
        listOf<Short>(1, 2, 3, 4, 5, 6, 7, 8, 9),
        listOf<Short>(7, 8, 9, 1, 2, 3, 4, 5, 6),
        listOf<Short>(4, 5, 6, 7, 8, 9, 1, 2, 3),

        listOf<Short>(9, 1, 2, 3, 4, 5, 6, 7, 8),
        listOf<Short>(6, 7, 8, 9, 1, 2, 3, 4, 5),
        listOf<Short>(3, 4, 5, 6, 7, 8, 9, 1, 2),

        listOf<Short>(8, 9, 1, 2, 3, 4, 5, 6, 7),
        listOf<Short>(5, 6, 7, 8, 9, 1, 2, 3, 4),
        listOf<Short>(2, 3, 4, 5, 6, 7, 8, 9, 1)
    )

    private val grid: Grid = gridValues.map { row ->
        row.map { Pair(it, false) }
    }

    private val gridRepositoryValues = gridValues.joinToString("") { it.joinToString("") }

    private val fixedValues = 0

    private val gridId = 0L

    private val gridRepository = mockk<GridRepository> {
        every { createGrid(date, gridRepositoryValues, fixedValues) }.returns(gridId)
    }

    private val isValidGridUseCase = mockk<IsValidGridUseCase> {
        every { this@mockk.invoke(grid) }.returns(true)
    }

    private val createGridUseCase = CreateGridUseCase(gridRepository, isValidGridUseCase)

    @Before
    fun before() {
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() }.returns(mockk { every { time }.returns(date) })
    }

    @Test
    fun testInvoke() {
        assertEquals(gridId, createGridUseCase())
    }

    @After
    fun after() {
        unmockkStatic(Calendar::class)
    }
}