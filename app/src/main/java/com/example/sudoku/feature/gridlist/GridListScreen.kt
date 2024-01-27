package com.example.sudoku.feature.gridlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject

@Composable
fun GridComposable(
    viewModel: GridListViewModel = koinInject(),
    onGridMetadataClick: (Long)->Unit) {

    val state by viewModel.stateFlow.collectAsState(initial = GridListViewModel.State.Idle)

    GridListScreen(
        state = state,
        onGridMetadataClick = onGridMetadataClick
    ) {
        viewModel.run(GridListViewModel.Action.Create)
    }
}

@Composable
private fun GridListScreen(
    state: GridListViewModel.State,
    onGridMetadataClick: (Long)->Unit = {},
    onCreateClick: ()->Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            GridListViewModel.State.Idle -> Idle(Modifier.align(Alignment.Center))
            is GridListViewModel.State.Loaded -> Loaded(state, onGridMetadataClick)
        }
         FloatingActionButton(
                modifier = Modifier.padding(15.dp).align(Alignment.BottomEnd),
                shape = CircleShape,
                onClick = { onCreateClick() }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add a new grid")
            }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    GridListScreen(GridListViewModel.State.Idle)
}