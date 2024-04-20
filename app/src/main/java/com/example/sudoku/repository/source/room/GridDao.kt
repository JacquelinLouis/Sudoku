package com.example.sudoku.repository.source.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GridDao {

    // GridMetadataEntity
    @Insert
    fun insert(gridMetadataEntity: GridMetadataEntity): Long

    @Query("SELECT * FROM GridMetadataEntity")
    fun get(): Flow<List<GridMetadataEntity>>

    @Update
    fun update(gridMetadataEntity: GridMetadataEntity)

    @Delete
    fun delete(gridMetadataEntity: GridMetadataEntity)

    // GridDataEntity
    @Insert
    fun insert(gridDataEntity: GridDataEntity): Long

    @Query("SELECT * FROM GridDataEntity WHERE gridMetadataId=:gridMetadataId")
    fun get(gridMetadataId: Long): Flow<GridDataEntity?>

    @Update
    fun update(gridDataEntity: GridDataEntity)

    @Delete
    fun delete(gridDataEntity: GridDataEntity)
}