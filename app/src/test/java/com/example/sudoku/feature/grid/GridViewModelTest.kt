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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GridViewModelTest {

    private val gridMetadataId = 0L

    private val getGridStateUseCase = mockk<GetGridStateUseCase>()

    private val updateGridDataUseCase = mockk<UpdateGridDataUseCase>(relaxUnitFun = true)

    private val deleteGridUseCase = mockk<DeleteGridUseCase>(relaxUnitFun = true)

    private fun viewModel(coroutineScope: CoroutineScope) = GridViewModel(
        gridMetadataId = gridMetadataId,
        getGridStateUseCase = getGridStateUseCase,
        updateGridDataUseCase = updateGridDataUseCase,
        deleteGridUseCase = deleteGridUseCase,
        coroutineUseCase = CoroutineUseCase(coroutineScope)
    )

    @Test
    fun testGetNullState() {
        coEvery { getGridStateUseCase(gridMetadataId) }.returns(flowOf(null))

        val result = runBlocking { viewModel(this).stateFlow.first() }

        assertEquals(result, GridViewModel.State.Success)
    }

    @Test
    fun testGetIdleState() {
        val gridMetadataId = 0L
        val grid = listOf(emptyList<Digit>())
        val gridFlow = flowOf(GetGridStateUseCase.State.Idle(grid))
        coEvery { getGridStateUseCase(gridMetadataId) }.returns(gridFlow)

        val result = runBlocking { viewModel(this).stateFlow.first() }

        assertEquals(result, GridViewModel.State.Idle(grid))
    }

    @Test
    fun testDeleteGrid() {
        val gridMetadataId = 0L
        val grid = listOf(emptyList<Digit>())
        val gridFlow = flowOf(GetGridStateUseCase.State.Success(grid))
        coEvery { getGridStateUseCase(gridMetadataId) }.returns(gridFlow)

        runBlocking { viewModel(this).stateFlow.first() }

        coVerify { deleteGridUseCase(gridMetadataId) }
    }

    @Test
    fun testGetSuccessState() {
        val gridMetadataId = 0L
        val grid = listOf(emptyList<Digit>())
        val gridFlow = flowOf(GetGridStateUseCase.State.Success(grid))
        coEvery { getGridStateUseCase(gridMetadataId) }.returns(gridFlow)

        val result = runBlocking { viewModel(this).stateFlow.first() }

        assertEquals(result, GridViewModel.State.Success)
    }

    @Test
    fun testUpdateGrid() {
        val grid = listOf(listOf<Digit>())
        val action = GridViewModel.Action.UpdateGrid(
            gridMetadataId,
            grid
        )
        coEvery { getGridStateUseCase(gridMetadataId) }.returns(emptyFlow())

        runBlocking { viewModel(this).run(action) }

        coVerify { updateGridDataUseCase(gridMetadataId, grid) }
    }
}