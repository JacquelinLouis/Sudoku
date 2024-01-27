package com.example.sudoku.feature.gridlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sudoku.repository.source.room.GridMetadataEntity
import com.example.sudoku.usecase.CreateGridUseCase
import com.example.sudoku.usecase.GetGridsMetadataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GridListViewModel(
    getGridsMetadataUseCase: GetGridsMetadataUseCase,
    private val createGridUseCase: CreateGridUseCase
): ViewModel() {

    interface State {
        data object Idle: State
        data class Loaded(val gridsMetadata: List<GridMetadataEntity>): State
    }

    interface Action {
        data object Create: Action
    }

    val stateFlow = getGridsMetadataUseCase().map { State.Loaded(it) }

    fun run(action: Action) {
        if (action is Action.Create)
            viewModelScope.launch(Dispatchers.IO) { createGridUseCase() }
    }
}