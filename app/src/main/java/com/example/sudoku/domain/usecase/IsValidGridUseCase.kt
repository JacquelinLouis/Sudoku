package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.Grid
import com.example.sudoku.domain.data.isValid

class IsValidGridUseCase {

    operator fun invoke(grid: Grid): Boolean {
        grid.forEachIndexed { x, row ->
            row.forEachIndexed { y, _ ->
                if (!grid.isValid(x, y)) return false
            }
        }
        return true
    }

}