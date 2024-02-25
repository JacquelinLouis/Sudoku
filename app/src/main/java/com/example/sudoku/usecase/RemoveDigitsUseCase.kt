package com.example.sudoku.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import com.example.sudoku.Config.Companion.GRID_SIZE
import kotlin.random.Random

class RemoveDigitsUseCase {

    operator fun invoke(grid: Grid, number: Int): Grid {
        assert(number <= GRID_SIZE)
        return grid
            .map { it.toMutableList() }
            .let { mutableGrid ->
                var counter = 0
                while (counter < number) {
                    val x = Random.nextInt(GRID_LENGTH)
                    val y = Random.nextInt(GRID_LENGTH)
                    if (mutableGrid[x][y].value > 0.toShort()) {
                        mutableGrid[x][y] = Digit(0, false)
                        counter += 1
                    }
                }
                mutableGrid
            }
    }
}