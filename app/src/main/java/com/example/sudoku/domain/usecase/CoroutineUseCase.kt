package com.example.sudoku.domain.usecase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * CoroutineScope provider, which can provide an optional coroutine scope to be used
 * (in tests for instance).
 * Instance should still be provided to be retrieved by Koin DSL constructor.
 */
data class CoroutineUseCase(val coroutineScope: CoroutineScope? = null) {

    operator fun invoke(
        viewModel: ViewModel,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = (coroutineScope ?: viewModel.viewModelScope).launch(context, start, block)

}