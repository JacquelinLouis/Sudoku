package com.example.sudoku.feature.gridlist

import com.example.sudoku.domain.data.GridMetadata
import com.example.sudoku.domain.usecase.CreateGridUseCase
import com.example.sudoku.domain.usecase.GetGridsMetadataUseCase
import com.example.sudoku.feature.CoroutineScopeProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GridListViewModelTest {

    private val getGridsMetadataUseCase = mockk<GetGridsMetadataUseCase>()

    private val createGridUseCase = mockk<CreateGridUseCase>()

    private fun gridViewModel(coroutineScope: CoroutineScope) = GridListViewModel(
        getGridsMetadataUseCase = getGridsMetadataUseCase,
        createGridUseCase = createGridUseCase,
        coroutineScopeProvider = CoroutineScopeProvider(coroutineScope)
    )

    @Test
    fun testLoadedState() {
        val list = emptyList<GridMetadata>()
        every { getGridsMetadataUseCase() }.returns(flowOf(list))

        val state = runBlocking { gridViewModel(this).stateFlow.first() }

        assertEquals(GridListViewModel.State.Loaded(list), state)
    }

    @Test
    fun testCreatedState() {
        val createdId = 0L
        every { getGridsMetadataUseCase() }.returns(flowOf(emptyList()))
        every { createGridUseCase() }.returns(createdId)


        val state = runBlocking {
            val viewModel = gridViewModel(this).apply { run(GridListViewModel.Action.Create) }
            viewModel.stateFlow.first()
        }

        assertEquals(GridListViewModel.State.Created(createdId), state)
    }
}