package com.example.sudoku.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.Config.Companion.SUB_GRID_LENGTH

class IsValidGridUseCase {

    private fun Grid.isValidRow(rowIndex: Int): Boolean {
        val readValues = MutableList<Short>(GRID_LENGTH) { 0 }
        get(rowIndex).forEach { value ->
            if (readValues.contains(value.first)) return false
            readValues.add(value.first)
        }
        return true
    }

    private fun Grid.isValidColumn(columnIndex: Int): Boolean {
        val readValues = MutableList<Short>(GRID_LENGTH) { 0 }
        for (i in 0 until GRID_LENGTH) {
            get(i)[columnIndex].first.also { value ->
                if (readValues.contains(value)) return false
                readValues.add(value)
            }
        }
        return true
    }

    private fun Grid.isValidSubGrid(rowIndex: Int, columnIndex: Int): Boolean {
        val readValues = MutableList<Short>(GRID_LENGTH) { 0 }
        val minRowIndex = rowIndex - (rowIndex % SUB_GRID_LENGTH)
        val maxRowIndex = minRowIndex + SUB_GRID_LENGTH
        val minColumnIndex = columnIndex - (columnIndex % SUB_GRID_LENGTH)
        val maxColumnIndex = minColumnIndex + SUB_GRID_LENGTH
        for (y in (minColumnIndex until maxColumnIndex)) {
            for (x in (minRowIndex until maxRowIndex)) {
                val value = get(x)[y].first
                if (readValues.contains(value)) return false
                readValues.add(value)
            }
        }
        return true
    }

    private fun Grid.isValid(x: Int, y: Int) =
        isValidRow(x) && isValidColumn(y) && isValidSubGrid(x, y)

    operator fun invoke(grid: Grid): Boolean {
        for (x in 0 until GRID_LENGTH) {
            for (y in 0 until GRID_LENGTH) {
                if (!grid.isValid(x, y)) return false
            }
        }
        return true
    }

}