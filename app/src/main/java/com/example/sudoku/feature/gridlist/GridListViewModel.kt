package com.example.sudoku.feature.gridlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku.repository.source.room.GridMetadataEntity
import com.example.sudoku.domain.usecase.CreateGridUseCase
import com.example.sudoku.domain.usecase.GetGridsMetadataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GridListViewModel(
    getGridsMetadataUseCase: GetGridsMetadataUseCase,
    private val createGridUseCase: CreateGridUseCase
): ViewModel() {

    sealed class State {
        data object Idle: State()
        data class Loaded(val gridsMetadata: List<GridMetadataEntity>): State()
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
            viewModelScope.launch(Dispatchers.IO) {
                val gridMetadataId = createGridUseCase()
                _createdFlow.emit(gridMetadataId)
            }
    }
}