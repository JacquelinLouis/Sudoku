package com.example.sudoku.domain.usecase

import com.example.sudoku.domain.data.GridMetadata
import com.example.sudoku.repository.GridRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.assertEquals
import org.junit.Test

class GetGridsMetadataUseCaseTest {

    private val gridMetadata = mockk<Flow<List<GridMetadata>>>()

    private val gridRepository = mockk<GridRepository> {
        every { getGridsMetadata() }.returns(gridMetadata)
    }

    private val getGridsMetadataUseCase = GetGridsMetadataUseCase(gridRepository)

    @Test
    fun testInvoke() {
        assertEquals(gridMetadata, getGridsMetadataUseCase())
    }

}