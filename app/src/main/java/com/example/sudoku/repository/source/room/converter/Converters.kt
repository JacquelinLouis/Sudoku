package com.example.sudoku.repository.source.room.converter

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    @TypeConverter
    fun longToDate(long: Long): Date = Date(long)

    @TypeConverter
    fun dateToLong(date: Date): Long = date.time
}