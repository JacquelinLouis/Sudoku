package com.example.sudoku.domain.usecase

import com.example.sudoku.Config

data class Digit(val value: Short = 0, val fixed: Boolean = false)

typealias Grid = List<List<Digit>>

fun Grid.toPrettyString() = joinToString(separator = "\n") { row ->
    row.joinToString { digit -> digit.value.toString() }
}

private fun Grid.isValidRow(rowIndex: Int, value: Int): Boolean {
    assert(0 <= value && value <= Config.GRID_LENGTH)
    val readValues = Array(Config.GRID_LENGTH + 1) { index -> index == value }

    get(rowIndex).forEach { digit ->
        digit.value.toInt().let { digitValue ->
            if (0 < digitValue && readValues[digitValue]) return false
            readValues[digitValue] = true
        }
    }
    return true
}

private fun Grid.isValidColumn(columnIndex: Int, value: Int): Boolean {
    assert(0 <= value && value <= Config.GRID_LENGTH)
    val readValues = Array(Config.GRID_LENGTH + 1) { index -> index == value }

    for (i in 0 until Config.GRID_LENGTH) {
        get(i)[columnIndex].value.toInt().also { digitValue ->
            if (0 < digitValue && readValues[digitValue]) return false
            readValues[digitValue] = true
        }
    }
    return true
}

private fun Grid.isValidRegion(rowIndex: Int, columnIndex: Int, value: Int): Boolean {
    assert(0 <= value && value <= Config.GRID_LENGTH)
    val readValues = Array(Config.GRID_LENGTH + 1) { index -> index == value }

    val minRowIndex = rowIndex - (rowIndex % Config.SUB_GRID_LENGTH)
    val maxRowIndex = minRowIndex + Config.SUB_GRID_LENGTH
    val minColumnIndex = columnIndex - (columnIndex % Config.SUB_GRID_LENGTH)
    val maxColumnIndex = minColumnIndex + Config.SUB_GRID_LENGTH

    for (y in (minColumnIndex until maxColumnIndex)) {
        for (x in (minRowIndex until maxRowIndex)) {
            val digitValue = get(x)[y].value.toInt()
            if (0 < digitValue && readValues[digitValue]) return false
            readValues[digitValue] = true
        }
    }
    return true
}

fun Grid.isValid(x: Int, y: Int, value: Int = 0) = isValidRow(x, value)
    && isValidColumn(y, value)
    && isValidRegion(x, y, value)