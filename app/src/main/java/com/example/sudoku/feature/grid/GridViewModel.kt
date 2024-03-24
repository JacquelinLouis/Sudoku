package com.example.sudoku.feature.grid

import androidx.lifecycle.ViewModel
import com.example.sudoku.domain.data.Grid
import com.example.sudoku.domain.usecase.GetGridDataUseCase
import com.example.sudoku.domain.usecase.UpdateGridDataUseCase
import com.example.sudoku.feature.CoroutineScopeProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GridViewModel(
    private val getGridDataUseCase: GetGridDataUseCase,
    private val updateGridDataUseCase: UpdateGridDataUseCase,
    private val coroutineScopeProvider: CoroutineScopeProvider
): ViewModel() {

    sealed interface Action {
        data class UpdateGrid(val gridMetadataId: Long, val grid: Grid): Action
    }

    fun getGrid(gridMetadataId: Long): Flow<Grid> = getGridDataUseCase(gridMetadataId)

    fun run(action: Action) {
        if (action is Action.UpdateGrid) {
            coroutineScopeProvider.getViewModelScope(this).launch {
                updateGridDataUseCase(action.gridMetadataId, action.grid)
            }
        }
    }
}