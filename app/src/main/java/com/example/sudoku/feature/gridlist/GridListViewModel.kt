package com.example.sudoku.feature.gridlist

import androidx.lifecycle.ViewModel
import com.example.sudoku.repository.source.room.GridMetadataEntity
import com.example.sudoku.usecase.GetGridsMetadataUseCase
import kotlinx.coroutines.flow.map

class GridListViewModel(
    getGridsMetadataUseCase: GetGridsMetadataUseCase
): ViewModel() {

    interface State {
        data object Idle: State
        data class Loaded(val gridsMetadata: List<GridMetadataEntity>): State
    }

    val stateFlow = getGridsMetadataUseCase().map { State.Loaded(it) }
}