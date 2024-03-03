package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.usecase.GenerateGridUseCase
import com.example.sudoku.domain.usecase.RemoveDigitsUseCase
import org.junit.Test
import kotlin.test.assertEquals

class RemoveDigitsUseCaseTest {

    private val removeDigitsUseCase = RemoveDigitsUseCase()

    @Test
    fun testInvoke() {
        val expectedRemovedDigits = 9
        val generatedGrid = GenerateGridUseCase().invoke()

        val grid = removeDigitsUseCase(generatedGrid, expectedRemovedDigits)

        val removedDigits = grid.sumOf { row ->
            var counter = 0
            row.forEach { digit ->
                if (digit.value == 0.toShort())
                    counter += 1
            }
            counter
        }
        assertEquals(expectedRemovedDigits, removedDigits)
    }

}