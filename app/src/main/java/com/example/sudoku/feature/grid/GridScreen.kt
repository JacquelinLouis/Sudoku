package com.example.sudoku.feature.grid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudoku.usecase.Grid
import org.koin.compose.koinInject

@Composable
fun GridComposable(
    gridMetadataId: Long,
    viewModel: GridViewModel = koinInject(),
) {
    val grid by viewModel.getGrid(gridMetadataId).collectAsState(initial = null)

    GridScreen(grid = grid)
}

@Composable
fun GridScreen(grid: Grid?) {
    if (grid == null)
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Loading grid",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    else
        GridComponent(grid)
}

@Composable
private fun GridComponent(grid: Grid) {
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
                        TextButton(
                            modifier = Modifier
                                .weight(1F)
                                .aspectRatio(1F),
                            onClick = { /*TODO*/ }
                        ) {
                            Text(
                                text = column.first.toString(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .weight(1F)
                                    .aspectRatio(1F)
                            )
                        }
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
                Pair((index % 10).toShort(), index % 3 == 0)
            }
        }
    GridScreen(null)// grid)
}