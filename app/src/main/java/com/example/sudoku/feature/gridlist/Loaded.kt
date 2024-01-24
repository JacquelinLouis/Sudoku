package com.example.sudoku.feature.gridlist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudoku.repository.source.room.GridMetadataEntity
import java.util.Date

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

@Preview
@Composable
private fun Preview() {
    val loaded = GridListViewModel.State.Loaded(
        MutableList(3) { index -> GridMetadataEntity(index.toLong(), Date(index.toLong())) }
    )
    Loaded(loaded = loaded, onGridMetadataClick = {})
}