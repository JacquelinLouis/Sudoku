package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.usecase.GenerateGridUseCase
import com.example.sudoku.domain.usecase.IsValidGridUseCase
import com.example.sudoku.domain.usecase.toPrettyString
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