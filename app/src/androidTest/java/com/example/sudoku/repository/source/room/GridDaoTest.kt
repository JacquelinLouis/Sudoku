package com.example.sudoku.repository.source.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class GridDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var gridDao: GridDao

    @Before
    fun before() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        gridDao = appDatabase.gridDao()
    }

    @Test
    fun testGridMetadataEntityInsert() {
        val gridMetadataEntity = GridMetadataEntity(1L, Date(1234))
        gridDao.insert(gridMetadataEntity)

        val actual = runBlocking { gridDao.get().first() }

        assertEquals(listOf(gridMetadataEntity), actual)
    }

    @Test
    fun testGridMetadataEntityUpdate() {
        val gridMetadataEntity = GridMetadataEntity(1L, Date(1234))
        val updatedGridMetadataEntity = gridMetadataEntity.copy(1L, Date(5678))
        gridDao.insert(gridMetadataEntity)
        gridDao.update(updatedGridMetadataEntity)

        val actual = runBlocking { gridDao.get().first() }

        assertEquals(listOf(updatedGridMetadataEntity), actual)
    }

    @Test
    fun testGridMetadataEntityDelete() {
        val gridMetadataEntity = GridMetadataEntity(1L, Date(1234))
        gridDao.insert(gridMetadataEntity)
        gridDao.delete(gridMetadataEntity)

        val actual = runBlocking { gridDao.get().first() }

        assertEquals(emptyList<GridMetadataEntity>(), actual)
    }

    @Test
    fun testGridDataEntityInsert() {
        val gridMetadataEntity = GridMetadataEntity(1L, Date(1234))
        val gridDataEntity = GridDataEntity(1L, gridMetadataEntity.gridMetadataId, "", "")
        gridDao.insert(gridMetadataEntity)
        gridDao.insert(gridDataEntity)

        val actual = runBlocking { gridDao.get(gridMetadataEntity.gridMetadataId).first() }

        assertEquals(gridDataEntity, actual)
    }

    @Test
    fun testGridDataEntityUpdate() {
        val gridMetadataEntity = GridMetadataEntity(1L, Date(1234))
        val gridDataEntity = GridDataEntity(1L, gridMetadataEntity.gridMetadataId, "", "")
        val updatedGridDataEntity = GridDataEntity(1L, gridMetadataEntity.gridMetadataId, "1234", "0101")
        gridDao.insert(gridMetadataEntity)
        gridDao.insert(gridDataEntity)
        gridDao.update(updatedGridDataEntity)

        val actual = runBlocking { gridDao.get(gridMetadataEntity.gridMetadataId).first() }

        assertEquals(updatedGridDataEntity, actual)
    }

    @Test
    fun testGridDataEntityDelete() {
        val gridMetadataEntity = GridMetadataEntity(1L, Date(1234))
        val gridDataEntity = GridDataEntity(1L, gridMetadataEntity.gridMetadataId, "", "")
        gridDao.insert(gridMetadataEntity)
        gridDao.insert(gridDataEntity)
        gridDao.delete(gridDataEntity)

        val actual = runBlocking { gridDao.get(gridMetadataEntity.gridMetadataId).firstOrNull() }

        assertEquals(null, actual)
    }

    @Test
    fun testGridDataEntityDeleteCascade() {
        val gridMetadataEntity = GridMetadataEntity(1L, Date(1234))
        val gridDataEntity = GridDataEntity(1L, gridMetadataEntity.gridMetadataId, "", "")
        gridDao.insert(gridMetadataEntity)
        gridDao.insert(gridDataEntity)
        gridDao.delete(gridMetadataEntity)

        val actual = runBlocking { gridDao.get(gridMetadataEntity.gridMetadataId).firstOrNull() }

        assertEquals(null, actual)
    }

    @After
    fun after() {
        appDatabase.close()
    }
}