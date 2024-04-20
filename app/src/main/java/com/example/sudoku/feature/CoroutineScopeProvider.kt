package com.example.sudoku.feature

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
data class CoroutineScopeProvider(val coroutineScope: CoroutineScope? = null) {

    fun launch(
        viewModel: ViewModel,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = (coroutineScope ?: viewModel.viewModelScope).launch(context, start, block)

}