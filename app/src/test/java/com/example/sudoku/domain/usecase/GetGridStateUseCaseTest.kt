package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.Digit
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GetGridStateUseCaseTest {

    private val gridMetadataId = 0L

    private val grid = listOf(emptyList<Digit>())

    private val getGridDataUseCase = mockk<GetGridDataUseCase> {
        every { this@mockk(gridMetadataId) }.returns(flowOf(grid))
    }

    private val isCompleteGridUseCase = mockk<IsCompleteGridUseCase> {
        every { this@mockk(grid) }.returns(false)
    }

    private val isValidGridUseCase = mockk<IsValidGridUseCase> {
        every { this@mockk(grid) }.returns(false)
    }

    private val getGridStateUseCase = GetGridStateUseCase(
        getGridDataUseCase = getGridDataUseCase,
        isCompleteGridUseCase = isCompleteGridUseCase,
        isValidGridUseCase = isValidGridUseCase
    )

    @Test
    fun testIdleState() {
        val gridState = runBlocking { getGridStateUseCase(gridMetadataId).first() }

        assertEquals(GetGridStateUseCase.State.Idle(grid), gridState)
    }

    @Test
    fun testIncompleteState() {
        every { isValidGridUseCase(grid) }.returns(true)

        val gridState = runBlocking { getGridStateUseCase(gridMetadataId).first() }

        assertEquals(GetGridStateUseCase.State.Idle(grid), gridState)
    }

    @Test
    fun testInvalidState() {
        every { isCompleteGridUseCase(grid) }.returns(true)

        val gridState = runBlocking { getGridStateUseCase(gridMetadataId).first() }

        assertEquals(GetGridStateUseCase.State.Idle(grid), gridState)
    }

    @Test
    fun testSuccessState() {
        every { isCompleteGridUseCase(grid) }.returns(true)
        every { isValidGridUseCase(grid) }.returns(true)

        val gridState = runBlocking { getGridStateUseCase(gridMetadataId).first() }

        assertEquals(GetGridStateUseCase.State.Success(grid), gridState)
    }

}