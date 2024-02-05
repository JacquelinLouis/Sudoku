package com.example.sudoku.feature.grid

import androidx.lifecycle.ViewModel
import com.example.sudoku.usecase.GetGridDataUseCase
import com.example.sudoku.usecase.Grid
import kotlinx.coroutines.flow.Flow

class GridViewModel(private val getGridDataUseCase: GetGridDataUseCase): ViewModel() {

    fun getGrid(gridMetadataId: Long): Flow<Grid> = getGridDataUseCase(gridMetadataId)

}