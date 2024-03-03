package com.example.sudoku.domain.data

import com.example.sudoku.repository.source.room.GridMetadataEntity
import java.util.Date

data class GridMetadata(
    val id: Long,
    val creation: Date
)