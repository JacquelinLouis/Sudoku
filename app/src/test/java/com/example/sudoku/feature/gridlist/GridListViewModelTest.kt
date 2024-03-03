package com.example.sudoku.feature.gridlist

import com.example.sudoku.repository.source.room.GridMetadataEntity
import com.example.sudoku.domain.usecase.CreateGridUseCase
import com.example.sudoku.domain.usecase.GetGridsMetadataUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GridListViewModelTest {

    private val getGridsMetadataUseCase = mockk<GetGridsMetadataUseCase>()

    private val createGridUseCase = mockk<CreateGridUseCase>()

    private val gridViewModel: GridListViewModel
        get() = GridListViewModel(
            getGridsMetadataUseCase = getGridsMetadataUseCase,
            createGridUseCase = createGridUseCase
        )

    @Test
    fun testLoadedState() {
        val list = emptyList<GridMetadataEntity>()
        every { getGridsMetadataUseCase() }.returns(flowOf(list))

        val state = runBlocking { gridViewModel.stateFlow.first() }

        assertEquals(GridListViewModel.State.Loaded(list), state)
    }

    @Test
    fun testCreatedState() {
        val createdId = 0L
        every { getGridsMetadataUseCase() }.returns(flowOf(emptyList()))
        every { createGridUseCase() }.returns(createdId)

        val viewModel = gridViewModel.apply { run(GridListViewModel.Action.Create) }
        val state = runBlocking { viewModel.stateFlow.first() }

        assertEquals(GridListViewModel.State.Created(createdId), state)
    }
}