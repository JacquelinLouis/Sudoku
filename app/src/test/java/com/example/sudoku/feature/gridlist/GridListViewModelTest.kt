package com.example.sudoku.feature.gridlist

import com.example.sudoku.domain.data.GridMetadata
import com.example.sudoku.domain.usecase.CreateGridUseCase
import com.example.sudoku.domain.usecase.GetGridsMetadataUseCase
import com.example.sudoku.domain.usecase.CoroutineUseCase
import com.example.sudoku.domain.usecase.DeleteGridUseCase
import com.example.sudoku.domain.usecase.GetCurrentGridIdUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class GridListViewModelTest {

    private val getGridsMetadataUseCase = mockk<GetGridsMetadataUseCase> {
        every { this@mockk() }.returns(flowOf(emptyList()))
    }

    private val createGridUseCase = mockk<CreateGridUseCase>(relaxUnitFun = true)

    private val deleteGridUseCase = mockk<DeleteGridUseCase>(relaxUnitFun = true)

    private val getCurrentGridIdUseCase = mockk<GetCurrentGridIdUseCase> {
        every { this@mockk() }.returns(MutableStateFlow(null))
    }

    private fun gridViewModel(coroutineScope: CoroutineScope) = GridListViewModel(
        getGridsMetadataUseCase = getGridsMetadataUseCase,
        createGridUseCase = createGridUseCase,
        coroutineUseCase = CoroutineUseCase(coroutineScope),
        deleteGridUseCase = deleteGridUseCase,
        getCurrentGridIdUseCase = getCurrentGridIdUseCase
    )

    @Test
    fun testLoadedState() {
        val state = runBlocking { gridViewModel(this).stateFlow.first() }

        assertEquals(GridListViewModel.State.Loaded(emptyList()), state)
    }

    @Test
    fun testCreateGrid() {
        runBlocking { gridViewModel(this).run(GridListViewModel.Action.Create) }

        verify { createGridUseCase() }
    }

    @Test
    fun testCreatedState() {
        val createdId = 0L
        every { getGridsMetadataUseCase() }
            .returns(flowOf(listOf(GridMetadata(createdId, Date(0)))))
        every { getCurrentGridIdUseCase() }.returns(MutableStateFlow(createdId))

        val state = runBlocking { gridViewModel(this).stateFlow.first() }

        assertEquals(GridListViewModel.State.Created(createdId), state)
    }

    @Test
    fun testDeleteGrid() {
        val gridMetadataId = 0L

        runBlocking {
            gridViewModel(this).run(GridListViewModel.Action.Delete(gridMetadataId))
        }

        coVerify { deleteGridUseCase(gridMetadataId) }
    }
}