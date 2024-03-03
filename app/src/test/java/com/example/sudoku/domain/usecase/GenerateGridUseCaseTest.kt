package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.toPrettyString
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