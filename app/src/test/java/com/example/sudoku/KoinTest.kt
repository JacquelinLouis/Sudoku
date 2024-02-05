package com.example.sudoku

import android.content.Context
import io.mockk.mockk
import org.junit.Test
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules

class KoinTest {

    private val context = mockk<Context>()

    @Test
    fun verifyKoinApp() {
        koinApplication {
            KoinModule.getInstance(context)
            checkModules()
        }
    }

}