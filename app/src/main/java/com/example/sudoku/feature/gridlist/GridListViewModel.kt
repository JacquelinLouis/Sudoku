package com.example.sudoku.feature.gridlist

import androidx.lifecycle.ViewModel
import com.example.sudoku.domain.data.GridMetadata
import com.example.sudoku.domain.usecase.CreateGridUseCase
import com.example.sudoku.domain.usecase.GetGridsMetadataUseCase
import com.example.sudoku.domain.usecase.CoroutineUseCase
import com.example.sudoku.domain.usecase.DeleteGridUseCase
import com.example.sudoku.domain.usecase.GetCurrentGridIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

class GridListViewModel(
    getGridsMetadataUseCase: GetGridsMetadataUseCase,
    private val createGridUseCase: CreateGridUseCase,
    private val coroutineUseCase: CoroutineUseCase,
    private val deleteGridUseCase: DeleteGridUseCase,
    getCurrentGridIdUseCase: GetCurrentGridIdUseCase
): ViewModel() {

    sealed class State {
        data object Idle: State()
        data class Loaded(val gridsMetadata: List<GridMetadata>): State()
        data class Created(val gridMetadataId: Long): State()
    }

    interface Action {
        data object Create: Action
        data class Delete(val gridMetadataId: Long): Action
    }

    val stateFlow = combine(getGridsMetadataUseCase(), getCurrentGridIdUseCase()) { gridsMetadata, createdId ->
        if (createdId != null && gridsMetadata.any { it.id == createdId })
            State.Created(createdId)
        else
            State.Loaded(gridsMetadata)
    }.distinctUntilChanged()

    fun run(action: Action) {
        when (action) {
            Action.Create -> coroutineUseCase(this, Dispatchers.IO) {
                createGridUseCase()
            }
            is Action.Delete -> coroutineUseCase(this, Dispatchers.IO) {
                deleteGridUseCase(action.gridMetadataId)
            }
        }
    }
}