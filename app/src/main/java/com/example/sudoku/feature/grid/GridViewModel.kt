package com.example.sudoku.feature.grid

import androidx.lifecycle.ViewModel
import com.example.sudoku.domain.usecase.GetGridDataUseCase
import com.example.sudoku.domain.usecase.Grid
import kotlinx.coroutines.flow.Flow

class GridViewModel(private val getGridDataUseCase: GetGridDataUseCase): ViewModel() {

    fun getGrid(gridMetadataId: Long): Flow<Grid> = getGridDataUseCase(gridMetadataId)

}