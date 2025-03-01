package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.Digit
import com.example.sudoku.domain.data.Grid
import com.example.sudoku.repository.GridRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.Date

class CreateGridUseCaseTest {

    private val date = Date(0)

    private val gridValues = listOf(
        listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
        listOf(7, 8, 9, 1, 2, 3, 4, 5, 6),
        listOf(4, 5, 6, 7, 8, 9, 1, 2, 3),

        listOf(9, 1, 2, 3, 4, 5, 6, 7, 8),
        listOf(6, 7, 8, 9, 1, 2, 3, 4, 5),
        listOf(3, 4, 5, 6, 7, 8, 9, 1, 2),

        listOf(8, 9, 1, 2, 3, 4, 5, 6, 7),
        listOf(5, 6, 7, 8, 9, 1, 2, 3, 4),
        listOf(2, 3, 4, 5, 6, 7, 8, 9, 1)
    )

    private val removedDigitsGridValues = gridValues.mapIndexed { rowIndex, row ->
        row.mapIndexed { digitIndex, digit ->
            if (rowIndex % 4 == 0 && digitIndex % 3 == 0)
                0
            else
                digit
        }
    }

    private fun List<List<Int>>.toGrid() = map { row ->
        row.map { value -> Digit(value, value != 0) }
    }

    private val grid: Grid = gridValues.toGrid()

    private val removedDigitsGrid = removedDigitsGridValues.toGrid()

    private val gridId = 0L

    private val removedDigits = 1

    private val gridRepository = mockk<GridRepository> {
        every { createGrid(date, removedDigitsGrid) }.returns(gridId)
        every { setCurrentGridId(gridId) }.returns(true)
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
    fun testGenerateGrid() {
        createGridUseCase()

        verify { generateGridUseCase() }
    }

    @Test
    fun testRemoveDigits() {
        createGridUseCase()

        verify { removeDigitsUseCase(grid, 1) }
    }

    @Test
    fun testCreateGrid() {
        createGridUseCase()

        verify { gridRepository.createGrid(date, removedDigitsGrid) }
    }

    @Test
    fun testSetCurrentGridId() {
        createGridUseCase()

        verify { gridRepository.setCurrentGridId(gridId) }
    }

    @After
    fun after() {
        unmockkStatic(Calendar::class)
    }
}