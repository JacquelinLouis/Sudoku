package com.example.sudoku.feature.gridlist

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudoku.domain.data.GridMetadata
import org.koin.compose.koinInject
import java.lang.IllegalStateException
import java.util.Date

@Composable
fun GridListComposable(
    viewModel: GridListViewModel = koinInject(),
    onGridMetadataClick: (Long)->Unit) {

    val state by viewModel.stateFlow.collectAsState(initial = GridListViewModel.State.Idle)
    // Ugly workaround to avoid navigating twice on recomposition
    var created by remember { mutableStateOf(false) }

    when (val localState = state) {
        is GridListViewModel.State.Created -> {
            if (!created)
                onGridMetadataClick(localState.gridMetadataId)
            created = true
        }
        else -> GridListScreen(
            state = localState,
            onGridMetadataClick = onGridMetadataClick,
            onCreateClick = { viewModel.run(GridListViewModel.Action.Create) },
            onGridMetadataDelete = { viewModel.run(GridListViewModel.Action.Delete(it)) }
        )
    }
}

@Composable
private fun GridListScreen(
    state: GridListViewModel.State,
    onGridMetadataClick: (Long)->Unit = {},
    onCreateClick: ()->Unit = {},
    onGridMetadataDelete: (Long)->Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            GridListViewModel.State.Idle -> Idle(Modifier.align(Alignment.Center))
            is GridListViewModel.State.Loaded -> Loaded(state, onGridMetadataClick, onGridMetadataDelete)
            else -> throw IllegalStateException("Unhandled state $state")
        }
         FloatingActionButton(
                modifier = Modifier
                    .padding(15.dp)
                    .align(Alignment.BottomEnd),
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
    val list = MutableList(3) { index ->
        GridMetadata(index.toLong(), Date(index.toLong()))
    }
    GridListScreen(GridListViewModel.State.Loaded(list))
}