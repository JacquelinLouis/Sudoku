package com.example.sudoku.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

/**
 * CoroutineScope provider, which can provide an optional coroutine scope to be used
 * (in tests for instance).
 * Instance should still be provided to be retrieved by Koin DSL constructor.
 */
data class CoroutineScopeProvider(val coroutineScope: CoroutineScope? = null) {

    fun getViewModelScope(viewModel: ViewModel) = coroutineScope ?: viewModel.viewModelScope

}