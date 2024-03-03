package com.example.sudoku.domain.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.Config.Companion.GRID_SIZE
import com.example.sudoku.domain.data.Digit
import com.example.sudoku.domain.data.Grid
import com.example.sudoku.domain.data.isValid

class GenerateGridUseCase {

    private class GridIndex {
        private var _y: Int = 0
        val y: Int get() = _y

        private var _x: Int = 0

        val x: Int get() = _x

        val index get() = y + (x * GRID_LENGTH)

        fun increment(): Boolean {
            when {
                y < GRID_LENGTH - 1 -> _y += 1
                x < GRID_LENGTH - 1-> {
                    _y = 0
                    _x += 1
                }
                else -> return false
            }
            return true
        }

        fun decrement(): Boolean {
            when {
                0 < y -> _y -= 1
                0 < x -> {
                    _y = GRID_LENGTH - 1
                    _x -= 1
                }
                else -> return false
            }
            return true
        }
    }

    operator fun invoke(): Grid {
        val grid = MutableList(GRID_LENGTH) {
            MutableList(GRID_LENGTH) {
                Digit(0, true)
            }
        }

        val possibleGridValues = MutableList(GRID_SIZE) { VALUES.toMutableList() }
        val gridIndex = GridIndex()

        do {
            val value = possibleGridValues[gridIndex.index].firstOrNull { value ->
                gridIndex.run { grid.isValid(x, y, value) }
            }
            if (value == null) {
                possibleGridValues[gridIndex.index] = VALUES.toMutableList()
                gridIndex.decrement()
                gridIndex.run {
                    possibleGridValues[index].apply { remove(grid[x][y].value) }
                    grid[x][y] = Digit(0, true)
                }
                gridIndex.decrement()
            } else {
                gridIndex.run { grid[x][y] = Digit(value, true) }
            }
        } while (gridIndex.increment())

        return grid
    }

    private companion object {
        private val VALUES = MutableList(GRID_LENGTH) { index -> index + 1 }
    }
}