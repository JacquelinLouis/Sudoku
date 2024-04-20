package com.example.sudoku.domain.usecase

import com.example.sudoku.repository.GridRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DeleteGridUseCaseTest {

    private val gridRepository = mockk<GridRepository>(relaxUnitFun = true)

    private val deleteGridUseCase = DeleteGridUseCase(gridRepository)

    @Test
    fun testInvoke() {
        val gridMetadataId = 0L

        runBlocking { deleteGridUseCase(gridMetadataId) }

        coVerify { gridRepository.deleteGrid(gridMetadataId) }
    }

}