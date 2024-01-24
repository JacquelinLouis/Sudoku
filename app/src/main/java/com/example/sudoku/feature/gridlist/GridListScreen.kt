package com.example.sudoku.feature.gridlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject

@Composable
fun GridListScreen(
    viewModel: GridListViewModel = koinInject(),
    onGridMetadataClick: (Long)->Unit
) {
    val state by viewModel.stateFlow.collectAsState(initial = GridListViewModel.State.Idle)

    when (val fixedState = state) {
        GridListViewModel.State.Idle -> Idle()
        is GridListViewModel.State.Loaded -> Loaded(fixedState, onGridMetadataClick)
    }
}