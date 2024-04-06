package com.example.sudoku.feature.grid

import com.example.sudoku.domain.data.Digit
import com.example.sudoku.domain.usecase.GetGridDataUseCase
import com.example.sudoku.domain.usecase.IsCompleteGridUseCase
import com.example.sudoku.domain.usecase.IsValidGridUseCase
import com.example.sudoku.domain.usecase.UpdateGridDataUseCase
import com.example.sudoku.feature.CoroutineScopeProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GridViewModelTest {

    private val getGridDataUseCase = mockk<GetGridDataUseCase>()

    private val updateGridDataUseCase = mockk<UpdateGridDataUseCase>(relaxUnitFun = true)

    private val isValidGridUseCase = mockk<IsValidGridUseCase> {
        every { this@mockk(any()) }.returns(true)
    }

    private val isCompleteGridUseCase = mockk<IsCompleteGridUseCase> {
        every { this@mockk(any()) }.returns(false)
    }

    private fun viewModel(coroutineScope: CoroutineScope) = GridViewModel(
        getGridDataUseCase = getGridDataUseCase,
        updateGridDataUseCase = updateGridDataUseCase,
        isValidGridUseCase = isValidGridUseCase,
        isCompleteGridUseCase = isCompleteGridUseCase,
        coroutineScopeProvider = CoroutineScopeProvider(coroutineScope)
    )

    @Test
    fun testGetState() {
        val gridMetadataId = 0L
        val grid = listOf(emptyList<Digit>())
        val gridFlow = flowOf(grid)
        coEvery { getGridDataUseCase(gridMetadataId) }.returns(gridFlow)
        val expected = GridViewModel.State.Idle(grid)

        val result = runBlocking { viewModel(this).getState(gridMetadataId).first() }

        assertEquals(result, expected)
    }

    @Test
    fun testUpdateGrid() {
        val gridMetadataId = 0L
        val grid = listOf(listOf<Digit>())
        val action = GridViewModel.Action.UpdateGrid(
            gridMetadataId,
            grid
        )

        runBlocking { viewModel(this).run(action) }

        coVerify { updateGridDataUseCase(gridMetadataId, grid) }
    }
}