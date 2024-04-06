package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.Grid
import kotlinx.coroutines.flow.map

class GetGridStateUseCase(
    private val getGridDataUseCase: GetGridDataUseCase,
    private val isCompleteGridUseCase: IsCompleteGridUseCase,
    private val isValidGridUseCase: IsValidGridUseCase
) {

    sealed class State(open val grid: Grid) {
        data class Idle(override val grid: Grid): State(grid)
        data class Success(override val grid: Grid): State(grid)
    }

    operator fun invoke(gridMetadataId: Long) = getGridDataUseCase(gridMetadataId).map { grid ->
        if (isCompleteGridUseCase(grid) && isValidGridUseCase(grid))
            State.Success(grid)
        else
            State.Idle(grid)
    }

}