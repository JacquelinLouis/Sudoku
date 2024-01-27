package com.example.sudoku

import android.content.Context
import com.example.sudoku.feature.gridlist.GridListViewModel
import com.example.sudoku.repository.GridRepository
import com.example.sudoku.repository.inspect.InspectorBroadcastReceiver
import com.example.sudoku.repository.source.room.AppDatabase
import com.example.sudoku.usecase.CreateGridUseCase
import com.example.sudoku.usecase.GetGridsMetadataUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.dsl.onClose

class KoinModule {

    companion object {
        fun getInstance(context: Context) = module {
            single { AppDatabase.getInstance(context) }
            single { get<AppDatabase>().gridDao() }
            singleOf(::GridRepository)
            single(createdAtStart = true) {
                InspectorBroadcastReceiver(
                    context = context,
                    gridRepository = get()
                )
            }.onClose { it?.close() }

            factoryOf(::GetGridsMetadataUseCase)
            factoryOf(::CreateGridUseCase)

            viewModelOf(::GridListViewModel)
        }

    }
}