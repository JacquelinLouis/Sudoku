package com.example.sudoku.feature.grid

import androidx.lifecycle.ViewModel
import com.example.sudoku.domain.data.Grid
import com.example.sudoku.domain.usecase.GetGridStateUseCase
import com.example.sudoku.domain.usecase.UpdateGridDataUseCase
import com.example.sudoku.feature.CoroutineScopeProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class GridViewModel(
    private val getGridStateUseCase: GetGridStateUseCase,
    private val updateGridDataUseCase: UpdateGridDataUseCase,
    private val coroutineScopeProvider: CoroutineScopeProvider
): ViewModel() {

    sealed interface Action {
        data class UpdateGrid(val gridMetadataId: Long, val grid: Grid): Action
    }

    sealed class State(open val grid: Grid) {
        data class Idle(override val grid: Grid): State(grid)

        data class Success(override val grid: Grid): State(grid)
    }

    fun getState(gridMetadataId: Long): Flow<State> = getGridStateUseCase(gridMetadataId)
        .map { state ->
            when(state) {
                is GetGridStateUseCase.State.Idle -> State.Idle(state.grid)
                is GetGridStateUseCase.State.Success -> State.Success(state.grid)
            }
        }

    fun run(action: Action) {
        if (action is Action.UpdateGrid) {
            coroutineScopeProvider.getViewModelScope(this).launch {
                updateGridDataUseCase(action.gridMetadataId, action.grid)
            }
        }
    }
}