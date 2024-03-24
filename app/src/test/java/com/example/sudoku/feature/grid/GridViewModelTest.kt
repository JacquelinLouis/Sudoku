package com.example.sudoku.feature.grid

import com.example.sudoku.domain.data.Digit
import com.example.sudoku.domain.data.Grid
import com.example.sudoku.domain.usecase.GetGridDataUseCase
import com.example.sudoku.domain.usecase.UpdateGridDataUseCase
import com.example.sudoku.feature.CoroutineScopeProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GridViewModelTest {

    private val getGridDataUseCase = mockk<GetGridDataUseCase>()

    private val updateGridDataUseCase = mockk<UpdateGridDataUseCase>(relaxUnitFun = true)

    private fun viewModel(coroutineScope: CoroutineScope) = GridViewModel(
        getGridDataUseCase = getGridDataUseCase,
        updateGridDataUseCase = updateGridDataUseCase,
        coroutineScopeProvider = CoroutineScopeProvider(coroutineScope)
    )

    @Test
    fun testGetGrid() {
        val gridMetadataId = 0L
        val gridFlow = emptyFlow<Grid>()
        coEvery { getGridDataUseCase(gridMetadataId) }.returns(gridFlow)

        val result = runBlocking { viewModel(this).getGrid(gridMetadataId) }

        assertEquals(result, gridFlow)
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