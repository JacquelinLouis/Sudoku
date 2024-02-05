package com.example.sudoku.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.repository.GridRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.Date

class CreateGridUseCaseTest {

    private val date = Date(0)

    private val values = MutableList(9 * 9) { index ->
        index % GRID_LENGTH + 1
    }.run { joinToString(separator = "") }

    private val fixedValues = 0

    private val gridId = 0L

    private val gridRepository = mockk<GridRepository> {
        every { createGrid(date, values, fixedValues) }.returns(gridId)
    }

    private val createGridUseCase = CreateGridUseCase(gridRepository)

    @Before
    fun before() {
        mockkStatic(Calendar::class)
        every { Calendar.getInstance() }.returns(mockk { every { time }.returns(date) })
    }

    @Test
    fun testInvoke() {
        assertEquals(gridId, createGridUseCase())
    }

    @After
    fun after() {
        unmockkStatic(Calendar::class)
    }
}