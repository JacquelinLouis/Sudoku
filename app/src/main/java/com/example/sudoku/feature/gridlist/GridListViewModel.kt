package com.example.sudoku.feature.gridlist

import androidx.lifecycle.ViewModel
import com.example.sudoku.domain.data.GridMetadata
import com.example.sudoku.domain.usecase.CreateGridUseCase
import com.example.sudoku.domain.usecase.GetGridsMetadataUseCase
import com.example.sudoku.domain.usecase.CoroutineUseCase
import com.example.sudoku.domain.usecase.DeleteGridUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

class GridListViewModel(
    getGridsMetadataUseCase: GetGridsMetadataUseCase,
    private val createGridUseCase: CreateGridUseCase,
    private val coroutineUseCase: CoroutineUseCase,
    private val deleteGridUseCase: DeleteGridUseCase
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

    private val _createdFlow = MutableStateFlow<Long?>(null)

    val stateFlow = combine(getGridsMetadataUseCase(), _createdFlow) { gridsMetadata, createdId ->
        if (createdId != null && gridsMetadata.any { it.id == createdId })
            State.Created(createdId)
        else
            State.Loaded(gridsMetadata)
    }.distinctUntilChanged()

    fun run(action: Action) {
        when (action) {
            Action.Create -> coroutineUseCase(this, Dispatchers.IO) {
                val gridMetadataId = createGridUseCase()
                _createdFlow.emit(gridMetadataId)
            }
            is Action.Delete -> coroutineUseCase(this, Dispatchers.IO) {
                deleteGridUseCase(action.gridMetadataId)
            }
        }
    }
}