package com.example.sudoku.usecase

import org.junit.Test

class GenerateGridUseCaseTest {

    private val generateGridUseCase = GenerateGridUseCase()

    private val isValidGridUseCase = IsValidGridUseCase()

    @Test
    fun testInvoke() {
        val grid = generateGridUseCase()

        assert(isValidGridUseCase(grid)) {
            "Invalid grid: ${grid.toPrettyString()}"
        }
    }

}