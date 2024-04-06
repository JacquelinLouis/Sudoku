package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.Grid

class IsValidGridUseCase(private val isValidGridValueUseCase: IsValidGridValueUseCase) {

    operator fun invoke(grid: Grid): Boolean {
        grid.forEachIndexed { x, row ->
            row.forEachIndexed { y, _ ->
                if (!isValidGridValueUseCase(grid, x, y)) return false
            }
        }
        return true
    }

}