package com.example.sudoku.domain.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.Config.Companion.SUB_GRID_LENGTH
import org.junit.Assert.assertFalse
import org.junit.Test

class IsValidGridUseCaseTest {

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

    private val grid = gridValues.grid

    private val List<List<Int>>.grid
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
        val invalidGridValues = mutableListOf<List<Int>>().apply {
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