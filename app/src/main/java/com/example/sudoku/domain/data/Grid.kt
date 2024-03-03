package com.example.sudoku.domain.data

typealias Grid = List<List<Digit>>

fun Grid.toPrettyString() = joinToString(separator = "\n") { row ->
    row.joinToString { digit -> digit.value.toString() }
}

private fun Grid.isValidRow(rowIndex: Int, value: Int): Boolean {
    assert(0 <= value && value <= com.example.sudoku.Config.GRID_LENGTH)
    val readValues = Array(com.example.sudoku.Config.GRID_LENGTH + 1) { index -> index == value }

    get(rowIndex).forEach { digit ->
        digit.value.let { digitValue ->
            if (0 < digitValue && readValues[digitValue]) return false
            readValues[digitValue] = true
        }
    }
    return true
}

private fun Grid.isValidColumn(columnIndex: Int, value: Int): Boolean {
    assert(0 <= value && value <= com.example.sudoku.Config.GRID_LENGTH)
    val readValues = Array(com.example.sudoku.Config.GRID_LENGTH + 1) { index -> index == value }

    for (i in 0 until com.example.sudoku.Config.GRID_LENGTH) {
        get(i)[columnIndex].value.also { digitValue ->
            if (0 < digitValue && readValues[digitValue]) return false
            readValues[digitValue] = true
        }
    }
    return true
}

private fun Grid.isValidRegion(rowIndex: Int, columnIndex: Int, value: Int): Boolean {
    assert(0 <= value && value <= com.example.sudoku.Config.GRID_LENGTH)
    val readValues = Array(com.example.sudoku.Config.GRID_LENGTH + 1) { index -> index == value }

    val minRowIndex = rowIndex - (rowIndex % com.example.sudoku.Config.SUB_GRID_LENGTH)
    val maxRowIndex = minRowIndex + com.example.sudoku.Config.SUB_GRID_LENGTH
    val minColumnIndex = columnIndex - (columnIndex % com.example.sudoku.Config.SUB_GRID_LENGTH)
    val maxColumnIndex = minColumnIndex + com.example.sudoku.Config.SUB_GRID_LENGTH

    for (y in (minColumnIndex until maxColumnIndex)) {
        for (x in (minRowIndex until maxRowIndex)) {
            val digitValue = get(x)[y].value
            if (0 < digitValue && readValues[digitValue]) return false
            readValues[digitValue] = true
        }
    }
    return true
}

fun Grid.isValid(x: Int, y: Int, value: Int = 0) = isValidRow(x, value)
        && isValidColumn(y, value)
        && isValidRegion(x, y, value)