package com.example.sudoku.domain.usecase

import com.example.sudoku.Config.Companion.GRID_LENGTH
import kotlin.random.Random
import kotlin.random.nextInt

class GeneratePossibleValues {

    operator fun invoke() = MutableList(GRID_LENGTH) { index -> index + 1 }.apply {
        // Randomly swap values
        for (currentIndex in 0 until GRID_LENGTH) {
            val otherIndex = Random.nextInt(0 until GRID_LENGTH)
            val otherValue = get(otherIndex)
            set(otherIndex, get(currentIndex))
            set(currentIndex, otherValue)
        }
    }

}