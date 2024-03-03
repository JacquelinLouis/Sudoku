package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.usecase.CreateGridUseCase
import com.example.sudoku.domain.usecase.Digit
import com.example.sudoku.domain.usecase.GenerateGridUseCase
import com.example.sudoku.domain.usecase.Grid
import com.example.sudoku.domain.usecase.RemoveDigitsUseCase
import com.example.sudoku.repository.GridRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
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

    private val removedDigitsGridValues = gridValues.mapIndexed { rowIndex, row ->
        row.mapIndexed { digitIndex, digit ->
            if (rowIndex % 4 == 0 && digitIndex % 3 == 0)
                0
            else
                digit
        }
    }

    private fun List<List<Short>>.toGrid() = map { row ->
        row.map { value -> Digit(value, value != 0.toShort()) }
    }

    private val grid: Grid = gridValues.toGrid()

    private val removedDigitsGrid = removedDigitsGridValues.toGrid()

    private val gridRepositoryValues = removedDigitsGridValues.joinToString("") { it.joinToString("") }

    private val fixedValues = removedDigitsGrid.joinToString("") { row -> row.joinToString("") { digit -> if (digit.fixed) "1" else "0" } }

    private val gridId = 0L

    private val removedDigits = 9

    private val gridRepository = mockk<GridRepository> {
        every { createGrid(date, gridRepositoryValues, fixedValues) }.returns(gridId)
    }

    private val generateGridUseCase = mockk<GenerateGridUseCase> {
        every { this@mockk.invoke() }.returns(grid)
    }

    private val removeDigitsUseCase = mockk<RemoveDigitsUseCase> {
        every { this@mockk.invoke(grid, removedDigits) }.returns(removedDigitsGrid)
    }

    private val createGridUseCase = CreateGridUseCase(
        gridRepository,
        generateGridUseCase,
        removeDigitsUseCase
    )

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