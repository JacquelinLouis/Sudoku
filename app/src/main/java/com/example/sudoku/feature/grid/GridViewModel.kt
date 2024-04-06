package com.example.sudoku.feature.grid

import androidx.lifecycle.ViewModel
import com.example.sudoku.domain.data.Grid
import com.example.sudoku.domain.usecase.GetGridDataUseCase
import com.example.sudoku.domain.usecase.IsCompleteGridUseCase
import com.example.sudoku.domain.usecase.IsValidGridUseCase
import com.example.sudoku.domain.usecase.UpdateGridDataUseCase
import com.example.sudoku.feature.CoroutineScopeProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GridViewModel(
    private val getGridDataUseCase: GetGridDataUseCase,
    private val updateGridDataUseCase: UpdateGridDataUseCase,
    private val isValidGridUseCase: IsValidGridUseCase,
    private val isCompleteGridUseCase: IsCompleteGridUseCase,
    private val coroutineScopeProvider: CoroutineScopeProvider
): ViewModel() {

    sealed interface Action {
        data class UpdateGrid(val gridMetadataId: Long, val grid: Grid): Action
    }

    sealed class State(open val grid: Grid) {
        data class Idle(override val grid: Grid): State(grid)

        data class Success(override val grid: Grid): State(grid)
    }

    private val successFlow = MutableStateFlow(false)

    fun getState(gridMetadataId: Long): Flow<State> = combine(getGridDataUseCase(gridMetadataId), successFlow) {
        grid, success ->
        if (success)
            State.Success(grid)
        else
            State.Idle(grid)
    }

    fun run(action: Action) {
        if (action is Action.UpdateGrid) {
            coroutineScopeProvider.getViewModelScope(this).launch {
                if (!isValidGridUseCase(action.grid)) {
                    // TODO: reset the grid with previous value
                    return@launch
                }
                if (isCompleteGridUseCase(action.grid)) {
                    successFlow.emit(true)
                }
                updateGridDataUseCase(action.gridMetadataId, action.grid)
            }
        }
    }
}