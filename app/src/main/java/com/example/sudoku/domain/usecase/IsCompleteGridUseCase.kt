package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.Grid

class IsCompleteGridUseCase(private val isValidGridUseCase: IsValidGridUseCase) {

    private fun isGridFilled(grid: Grid) = grid.none { row ->
        row.any { digit ->
            digit.value == 0
        }
    }

    operator fun invoke(grid: Grid) =
        isValidGridUseCase(grid) && isGridFilled(grid)

}