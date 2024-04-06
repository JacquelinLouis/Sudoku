package com.example.sudoku.domain.usecase

import com.example.sudoku.Config
import com.example.sudoku.domain.data.Digit
import org.junit.Assert
import org.junit.Test

class IsValidGridValueUseCaseTest {

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

    private val List<List<Int>>.grid
        get() = map { row ->
            row.map { value ->
                Digit(value, false)
            }
        }

    private val grid = gridValues.grid

    private val isValidGridValueUseCase = IsValidGridValueUseCase()

    @Test
    fun testValidRow() {
        (0 until Config.GRID_LENGTH).forEach { x ->
            Assert.assertTrue(isValidGridValueUseCase(grid, x, 0))
        }
    }

    @Test
    fun testInvalidRow() {
        val invalidGridValues = mutableListOf<List<Int>>().apply {
            add(listOf(1, 2, 3, 4, 5, 6, 7, 8, 1))
            addAll(gridValues.subList(1, gridValues.size))
        }
        val invalidGrid = invalidGridValues.grid

        Assert.assertFalse(isValidGridValueUseCase(invalidGrid, 0, 0))
    }

    @Test
    fun testValidColumn() {
        (0 until Config.GRID_LENGTH).forEach { y ->
            Assert.assertTrue(isValidGridValueUseCase(grid, 0, y))
        }
    }

    @Test
    fun testInvalidColumn() {
        val invalidGridValues = gridValues.toMutableList().apply {
            set(Config.GRID_LENGTH - 1, get(Config.GRID_LENGTH - 1).toMutableList().apply {
                set(size - 1, 9)
            })
        }
        val invalidGrid = invalidGridValues.grid

        Assert.assertFalse(isValidGridValueUseCase(
            grid = invalidGrid,
            x = Config.GRID_LENGTH - 1,
            y = Config.GRID_LENGTH - 1
        ))
    }

    @Test
    fun testValidRegion() {
        Assert.assertTrue(isValidGridValueUseCase(
            grid = grid,
            x = Config.SUB_GRID_LENGTH,
            y = Config.SUB_GRID_LENGTH
        ))
    }

    @Test
    fun testInvalidRegion() {
        val invalidGridValues = gridValues.toMutableList().apply {
            set(Config.SUB_GRID_LENGTH, get(Config.SUB_GRID_LENGTH).toMutableList().apply {
                set(Config.SUB_GRID_LENGTH, 9)
            })
        }
        val invalidGrid = invalidGridValues.grid

        Assert.assertFalse(isValidGridValueUseCase(
            grid = invalidGrid,
            x = Config.SUB_GRID_LENGTH,
            y = Config.SUB_GRID_LENGTH
        ))
    }
}