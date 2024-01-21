package com.example.sudoku.repository.source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sudoku.repository.source.room.converter.Converters

@Database(
    entities = [GridMetadataEntity::class, GridDataEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun gridDao(): GridDao

    companion object {
        private const val DATABASE_NAME = "app-database"

        fun getInstance(context: Context): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}