package com.example.sudoku.feature.grid

import androidx.lifecycle.ViewModel
import com.example.sudoku.domain.data.Grid
import com.example.sudoku.domain.usecase.DeleteGridUseCase
import com.example.sudoku.domain.usecase.GetGridStateUseCase
import com.example.sudoku.domain.usecase.UpdateGridDataUseCase
import com.example.sudoku.feature.CoroutineScopeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GridViewModel(
    private val getGridStateUseCase: GetGridStateUseCase,
    private val updateGridDataUseCase: UpdateGridDataUseCase,
    private val deleteGridUseCase: DeleteGridUseCase,
    private val coroutineScopeProvider: CoroutineScopeProvider
): ViewModel() {

    sealed interface Action {
        data class UpdateGrid(val gridMetadataId: Long, val grid: Grid): Action
    }

    sealed interface State {
        data class Idle(val grid: Grid): State

        data object Success: State
    }

    fun getState(gridMetadataId: Long): Flow<State?> = getGridStateUseCase(gridMetadataId)
        .map { state ->
            when(state) {
                null -> State.Success
                is GetGridStateUseCase.State.Idle -> State.Idle(state.grid)
                is GetGridStateUseCase.State.Success -> {
                    coroutineScopeProvider.launch(this, Dispatchers.IO) {
                        deleteGridUseCase(gridMetadataId)
                    }
                    State.Success
                }
            }
        }

    fun run(action: Action) {
        if (action is Action.UpdateGrid) {
            coroutineScopeProvider.launch(this) {
                updateGridDataUseCase(action.gridMetadataId, action.grid)
            }
        }
    }
}