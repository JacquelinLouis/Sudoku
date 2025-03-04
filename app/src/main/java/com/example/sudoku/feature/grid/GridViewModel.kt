package com.example.sudoku.feature.grid

import androidx.lifecycle.ViewModel
import com.example.sudoku.domain.data.Grid
import com.example.sudoku.domain.usecase.DeleteGridUseCase
import com.example.sudoku.domain.usecase.GetGridStateUseCase
import com.example.sudoku.domain.usecase.UpdateGridDataUseCase
import com.example.sudoku.domain.usecase.CoroutineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GridViewModel(
    private val gridMetadataId: Long,
    getGridStateUseCase: GetGridStateUseCase,
    private val updateGridDataUseCase: UpdateGridDataUseCase,
    private val deleteGridUseCase: DeleteGridUseCase,
    private val coroutineUseCase: CoroutineUseCase
): ViewModel() {

    sealed interface Action {
        data class UpdateGrid(val gridMetadataId: Long, val grid: Grid): Action
    }

    sealed interface State {
        data class Idle(val grid: Grid): State

        data object Success: State
    }

    val stateFlow: Flow<State?> = getGridStateUseCase(gridMetadataId)
        .map { state ->
            when(state) {
                null -> State.Success
                is GetGridStateUseCase.State.Idle -> State.Idle(state.grid)
                is GetGridStateUseCase.State.Success -> State.Success
            }
        }

    fun run(action: Action) {
        if (action is Action.UpdateGrid) {
            coroutineUseCase(this) {
                updateGridDataUseCase(action.gridMetadataId, action.grid)
            }
        }
    }

    init {
        coroutineUseCase(this, Dispatchers.Default) {
            stateFlow.collect { state ->
                if (state is State.Success) {
                    deleteGridUseCase(gridMetadataId)
                }
            }
        }
    }
}