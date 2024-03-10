package com.example.sudoku.domain.usecase

import org.junit.Test
import kotlin.test.assertTrue

class GeneratePossibleValuesTest {

    private val generatePossibleValues = GeneratePossibleValues()

    @Test
    fun testInvoke() {
        val expectedValues = MutableList(9) { index -> index + 1}

        val result = generatePossibleValues()

        assertTrue(result.containsAll(expectedValues))
    }

}