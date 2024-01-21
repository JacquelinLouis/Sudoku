package com.example.sudoku.feature.gridlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun Idle() {
    Text(text = "Create a new grid with the + button")
}

@Composable
fun Loaded(
    loaded: GridListViewModel.State.Loaded,
    onGridMetadataClick: (Long)->Unit
) {
    val gridsMetadata = loaded.gridsMetadata

    if (gridsMetadata.isEmpty())
        Idle()
    else
        LazyColumn {
            itemsIndexed(gridsMetadata) { index, gridMetadataEntity ->
                Button(onClick = { onGridMetadataClick(gridMetadataEntity.gridMetadataId) }) {
                    Text(text = "${gridMetadataEntity.creation.time}")
                }
                if (index < gridsMetadata.lastIndex) {
                    Divider(color = Color.Black, thickness = 1.dp)
                }
            }
        }
}

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