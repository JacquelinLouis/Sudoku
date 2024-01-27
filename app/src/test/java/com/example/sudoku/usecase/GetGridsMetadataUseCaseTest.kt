package com.example.sudoku.usecase

import com.example.sudoku.repository.GridRepository
import com.example.sudoku.repository.source.room.GridMetadataEntity
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.assertEquals
import org.junit.Test

class GetGridsMetadataUseCaseTest {

    private val gridMetadata = mockk<Flow<List<GridMetadataEntity>>>()

    private val gridRepository = mockk<GridRepository> {
        every { getGridsMetadata() }.returns(gridMetadata)
    }

    private val getGridsMetadataUseCase = GetGridsMetadataUseCase(gridRepository)

    @Test
    fun testInvoke() {
        assertEquals(gridMetadata, getGridsMetadataUseCase())
    }

}