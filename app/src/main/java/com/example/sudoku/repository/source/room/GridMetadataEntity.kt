package com.example.sudoku.repository.source.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class GridMetadataEntity(
    @PrimaryKey(autoGenerate = true)
    val gridMetadataId: Long = 0L,
    val creation: Date
)
