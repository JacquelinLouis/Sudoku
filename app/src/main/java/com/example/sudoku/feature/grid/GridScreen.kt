package com.example.sudoku.feature.grid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudoku.R
import com.example.sudoku.domain.data.Digit
import com.example.sudoku.domain.data.Grid
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun GridComposable(
    gridMetadataId: Long,
    onDismiss: () -> Unit,
    viewModel: GridViewModel = koinInject { parametersOf(gridMetadataId) },
) {
    val state by viewModel.stateFlow.collectAsState(initial = null)

    GridScreen(
        state = state,
        onGridChanged = { newGrid ->
            viewModel.run(GridViewModel.Action.UpdateGrid(gridMetadataId, newGrid))
        },
        onDismiss = onDismiss
    )
}

@Composable
fun GridScreen(
    state: GridViewModel.State?,
    onGridChanged: (Grid) -> Unit,
    onDismiss: () -> Unit
) {
    when (state) {
        is GridViewModel.State.Idle -> GridComponent(grid = state.grid, onGridChanged = onGridChanged)
        is GridViewModel.State.Success -> SuccessComponent(onDismiss = onDismiss)
        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Loading grid",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

private val DECIMALS = MutableList(9) { index -> (index + 1).toString() }

private fun String.filterDecimal(): String = takeIf { DECIMALS.contains(it) } ?: ""

private fun Grid.set(newValue: Int, rowIndex: Int, columnIndex: Int): Grid {
    val newRow = get(rowIndex).toMutableList().apply {
        set(columnIndex, Digit(newValue, false))
    }
    val newGrid = toMutableList().apply {
        set(rowIndex, newRow)
    }
    return newGrid
}

@Composable
private fun SuccessComponent(onDismiss: () -> Unit) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.grid_completed_title)) },
        text = { Text(text = stringResource(id = R.string.grid_completed_text)) },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GridComponent(
    grid: Grid,
    onGridChanged: (Grid) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            grid.forEachIndexed { rowIndex, row ->
                Divider(color = if (0 < rowIndex && rowIndex % 3 == 0) Color.Black else Color.LightGray)
                Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                    row.forEachIndexed { columnIndex, column ->
                        Divider(
                            modifier = Modifier
                                .width(1.dp)
                                .fillMaxHeight(),
                            color = if (0 < columnIndex && columnIndex % 3 == 0) Color.Black else Color.LightGray
                        )

                        var text by remember { mutableStateOf(column.value.takeIf { it != 0 }?.toString() ?: "") }

                        TextField(
                            value = text,
                            onValueChange = {
                                text = it.filterDecimal()
                                text.toIntOrNull()?.let { newValue ->
                                    onGridChanged(grid.set(newValue, rowIndex, columnIndex))
                                }
                            },
                            enabled = !column.fixed,
                            readOnly = column.fixed,
                            modifier = Modifier
                                .weight(1F)
                                .fillMaxWidth(),
                            textStyle = TextStyle(
                                color = if (column.fixed) Color.Black else Color.LightGray,
                                background = Color.White
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }
                }
            }
            Divider(color = Color.LightGray)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val grid = MutableList(9) { _ ->
            MutableList(9) { index ->
                Digit(
                    index % 10,
                    index % 3 == 0
                )
            }
        }
    GridScreen(
        state = GridViewModel.State.Idle(grid),
        onGridChanged = {},
        onDismiss = {}
    )
}