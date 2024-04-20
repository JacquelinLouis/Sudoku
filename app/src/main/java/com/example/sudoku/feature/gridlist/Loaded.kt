package com.example.sudoku.feature.gridlist

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sudoku.R
import com.example.sudoku.domain.data.GridMetadata
import java.util.Date

@Composable
fun Loaded(
    loaded: GridListViewModel.State.Loaded,
    onGridMetadataClick: (Long)->Unit = {},
    onGridMetadataDelete: (Long)->Unit = {}
) {
    val gridsMetadata = loaded.gridsMetadata

    if (gridsMetadata.isEmpty())
        Idle()
    else
        LazyColumn {
            itemsIndexed(gridsMetadata) { index, gridMetadataEntity ->
                Row(Modifier.fillMaxWidth()) {
                    Button(onClick = { onGridMetadataClick(gridMetadataEntity.id) }) {
                        Text(text = "${gridMetadataEntity.creation.time}")
                    }
                    Spacer(Modifier.weight(1f))
                    Button(onClick = { onGridMetadataDelete(gridMetadataEntity.id) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = stringResource(id = R.string.delete_icon_description, gridMetadataEntity.id))
                    }
                }
                if (index < gridsMetadata.lastIndex) {
                    Divider(color = Color.Black, thickness = 1.dp)
                }
            }
        }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val loaded = GridListViewModel.State.Loaded(
        MutableList(3) { index -> GridMetadata(index.toLong(), Date(index.toLong())) }
    )
    Loaded(loaded = loaded)
}