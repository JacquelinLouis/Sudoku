package com.example.sudoku.repository.source.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = GridMetadataEntity::class,
        parentColumns = arrayOf("gridMetadataId"),
        childColumns = arrayOf("gridMetadataId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class GridDataEntity(
    @PrimaryKey(autoGenerate = true)
    val gridDataId: Long = 0L,
    @ColumnInfo(index = true)
    val gridMetadataId: Long,
    val values: String,
    val fixedValues: Int,
)
