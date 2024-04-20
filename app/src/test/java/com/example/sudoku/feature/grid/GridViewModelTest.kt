package com.example.sudoku.feature.grid

import com.example.sudoku.domain.data.Digit
import com.example.sudoku.domain.usecase.DeleteGridUseCase
import com.example.sudoku.domain.usecase.GetGridStateUseCase
import com.example.sudoku.domain.usecase.UpdateGridDataUseCase
import com.example.sudoku.domain.usecase.CoroutineUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GridViewModelTest {

    private val getGridStateUseCase = mockk<GetGridStateUseCase>()

    private val updateGridDataUseCase = mockk<UpdateGridDataUseCase>(relaxUnitFun = true)

    private val deleteGridUseCase = mockk<DeleteGridUseCase>(relaxUnitFun = true)

    private fun viewModel(coroutineScope: CoroutineScope) = GridViewModel(
        getGridStateUseCase = getGridStateUseCase,
        updateGridDataUseCase = updateGridDataUseCase,
        deleteGridUseCase = deleteGridUseCase,
        coroutineUseCase = CoroutineUseCase(coroutineScope)
    )

    @Test
    fun testGetNullState() {
        val gridMetadataId = 0L
        coEvery { getGridStateUseCase(gridMetadataId) }.returns(flowOf(null))

        val result = runBlocking { viewModel(this).getState(gridMetadataId).first() }

        assertEquals(result, GridViewModel.State.Success)
    }

    @Test
    fun testGetIdleState() {
        val gridMetadataId = 0L
        val grid = listOf(emptyList<Digit>())
        val gridFlow = flowOf(GetGridStateUseCase.State.Idle(grid))
        coEvery { getGridStateUseCase(gridMetadataId) }.returns(gridFlow)

        val result = runBlocking { viewModel(this).getState(gridMetadataId).first() }

        assertEquals(result, GridViewModel.State.Idle(grid))
    }

    @Test
    fun testDeleteGrid() {
        val gridMetadataId = 0L
        val grid = listOf(emptyList<Digit>())
        val gridFlow = flowOf(GetGridStateUseCase.State.Success(grid))
        coEvery { getGridStateUseCase(gridMetadataId) }.returns(gridFlow)

        runBlocking { viewModel(this).getState(gridMetadataId).first() }

        coVerify { deleteGridUseCase(gridMetadataId) }
    }

    @Test
    fun testGetSuccessState() {
        val gridMetadataId = 0L
        val grid = listOf(emptyList<Digit>())
        val gridFlow = flowOf(GetGridStateUseCase.State.Success(grid))
        coEvery { getGridStateUseCase(gridMetadataId) }.returns(gridFlow)

        val result = runBlocking { viewModel(this).getState(gridMetadataId).first() }

        assertEquals(result, GridViewModel.State.Success)
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