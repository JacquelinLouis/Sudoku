package com.example.sudoku.domain.usecase

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsCompleteGridUseCaseTest {

    private val completeGrid = GenerateGridUseCase(
        GeneratePossibleValues(),
        IsValidGridValueUseCase()
    ).invoke()

    private val isCompleteGridUseCase = IsCompleteGridUseCase(
        IsValidGridUseCase(IsValidGridValueUseCase())
    )

    @Test
    fun testIsComplete() {
        assertTrue(
            actual = isCompleteGridUseCase(completeGrid),
            message = "Incomplete grid $completeGrid"
        )
    }

    @Test
    fun testIncomplete() {
        val incompleteGrid = RemoveDigitsUseCase().invoke(completeGrid, 1)

        assertFalse(
            actual = isCompleteGridUseCase(incompleteGrid),
            message = "Complete grid $completeGrid"
        )
    }
}