package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.toPrettyString
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class GenerateGridUseCaseTest {

    private val generatePossibleValues = mockk<GeneratePossibleValues> {
        every { this@mockk.invoke() }.returns(MutableList(9) { index -> index + 1 })
    }

    private val generateGridUseCase = GenerateGridUseCase(generatePossibleValues)

    private val isValidGridUseCase = IsValidGridUseCase()

    @Test
    fun testInvoke() {
        val grid = generateGridUseCase()

        assert(isValidGridUseCase(grid)) {
            "Invalid grid: ${grid.toPrettyString()}"
        }
    }

}