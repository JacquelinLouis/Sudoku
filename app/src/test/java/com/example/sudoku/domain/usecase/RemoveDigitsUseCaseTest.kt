package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.Digit
import org.junit.Test
import kotlin.test.assertEquals

class RemoveDigitsUseCaseTest {

    private val removeDigitsUseCase = RemoveDigitsUseCase()

    @Test
    fun testInvoke() {
        val expectedRemovedDigits = 9
        val generatedGrid = MutableList(9) { _ ->
            MutableList(9) { column ->
                Digit(column + 1, false)
            }
        }

        val grid = removeDigitsUseCase(generatedGrid, expectedRemovedDigits)

        val removedDigits = grid.sumOf { row ->
            var counter = 0
            row.forEach { digit ->
                if (digit.value == 0)
                    counter += 1
            }
            counter
        }
        assertEquals(expectedRemovedDigits, removedDigits)
    }

}