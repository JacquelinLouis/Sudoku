package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.toPrettyString
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class GenerateGridUseCaseTest {

    private val generatePossibleValues = mockk<GeneratePossibleValues> {
        every { this@mockk.invoke() }.returns(MutableList(9) { index -> index + 1 })
    }

    private val isValidGridValueUseCase = IsValidGridValueUseCase()

    private val generateGridUseCase = GenerateGridUseCase(
        generatePossibleValues = generatePossibleValues,
        isValidGridValueUseCase = isValidGridValueUseCase
    )

    private val isValidGridUseCase = IsValidGridUseCase(isValidGridValueUseCase)

    @Test
    fun testInvoke() {
        val grid = generateGridUseCase()

        assert(isValidGridUseCase(grid)) {
            "Invalid grid:\n${grid.toPrettyString()}"
        }
    }

}