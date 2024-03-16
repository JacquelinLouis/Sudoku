package com.example.sudoku.feature.gridlist

import androidx.lifecycle.ViewModel
import com.example.sudoku.domain.data.GridMetadata
import com.example.sudoku.domain.usecase.CreateGridUseCase
import com.example.sudoku.domain.usecase.GetGridsMetadataUseCase
import com.example.sudoku.feature.CoroutineScopeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GridListViewModel(
    getGridsMetadataUseCase: GetGridsMetadataUseCase,
    private val createGridUseCase: CreateGridUseCase,
    private val coroutineScopeProvider: CoroutineScopeProvider
): ViewModel() {

    sealed class State {
        data object Idle: State()
        data class Loaded(val gridsMetadata: List<GridMetadata>): State()
        data class Created(val gridMetadataId: Long): State()
    }

    interface Action {
        data object Create: Action
    }

    private val _createdFlow = MutableStateFlow<Long?>(null)

    val stateFlow = combine(getGridsMetadataUseCase(), _createdFlow) { gridsMetadata, createdId ->
        if (createdId != null)
            State.Created(createdId)
        else
            State.Loaded(gridsMetadata)
    }

    fun run(action: Action) {
        if (action is Action.Create)
            coroutineScopeProvider.getViewModelScope(this).launch(Dispatchers.IO) {
                val gridMetadataId = createGridUseCase()
                _createdFlow.emit(gridMetadataId)
            }
    }
}