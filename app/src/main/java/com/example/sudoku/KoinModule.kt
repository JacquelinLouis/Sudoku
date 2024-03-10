package com.example.sudoku

import android.content.Context
import com.example.sudoku.feature.grid.GridViewModel
import com.example.sudoku.feature.gridlist.GridListViewModel
import com.example.sudoku.repository.GridRepository
import com.example.sudoku.repository.inspect.InspectorBroadcastReceiver
import com.example.sudoku.repository.source.room.AppDatabase
import com.example.sudoku.domain.usecase.CreateGridUseCase
import com.example.sudoku.domain.usecase.GenerateGridUseCase
import com.example.sudoku.domain.usecase.GeneratePossibleValues
import com.example.sudoku.domain.usecase.GetGridDataUseCase
import com.example.sudoku.domain.usecase.GetGridsMetadataUseCase
import com.example.sudoku.domain.usecase.IsValidGridUseCase
import com.example.sudoku.domain.usecase.RemoveDigitsUseCase
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
            factoryOf(::GetGridDataUseCase)
            factoryOf(::IsValidGridUseCase)
            factoryOf(::GenerateGridUseCase)
            factoryOf(::RemoveDigitsUseCase)
            factoryOf(::GeneratePossibleValues)

            viewModelOf(::GridListViewModel)
            viewModelOf(::GridViewModel)
        }

    }
}