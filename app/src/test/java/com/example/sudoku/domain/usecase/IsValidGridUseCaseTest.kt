package com.example.sudoku.domain.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.Config.Companion.SUB_GRID_LENGTH
import com.example.sudoku.domain.usecase.Digit
import com.example.sudoku.domain.usecase.IsValidGridUseCase
import org.junit.Assert.assertFalse
import org.junit.Test

class IsValidGridUseCaseTest {

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

    private val grid = gridValues.grid

    private val List<List<Short>>.grid
        get() = map { row ->
            row.map { value ->
                Digit(value, false)
            }
        }

    private val isValidGridUseCase = IsValidGridUseCase()

    @Test
    fun testValidGrid() {
        assert(isValidGridUseCase(grid))
    }

    @Test
    fun testInvalidRow() {
        val invalidGridValues = mutableListOf<List<Short>>().apply {
            add(listOf(1, 2, 3, 4, 5, 6, 7, 8, 1))
            addAll(gridValues.subList(1, gridValues.size))
        }
        val invalidGrid = invalidGridValues.grid

        assertFalse(isValidGridUseCase(invalidGrid))
    }

    @Test
    fun testInvalidColumn() {
        val invalidGridValues = gridValues.toMutableList().apply {
            set(GRID_LENGTH - 1, get(GRID_LENGTH - 1).toMutableList().apply {
                set(size - 1, 9)
            })
        }
        val invalidGrid = invalidGridValues.grid

        assertFalse(isValidGridUseCase(invalidGrid))
    }

    @Test
    fun testInvalidRegion() {
        val invalidGridValues = gridValues.toMutableList().apply {
            set(SUB_GRID_LENGTH - 1, get(SUB_GRID_LENGTH - 1).toMutableList().apply {
                set(SUB_GRID_LENGTH - 1, 9)
            })
        }
        val invalidGrid = invalidGridValues.grid

        assertFalse(isValidGridUseCase(invalidGrid))
    }
}