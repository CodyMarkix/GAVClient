package com.markix.gavclient.logic.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markix.gavclient.logic.repos.SeedColorRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val repository: SeedColorRepository
) : ViewModel() {

    // Expose the flow as StateFlow for the UI
    val seedColor: StateFlow<Long?> = repository.seedColor
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun updateSeedColor(color: Long) {
        viewModelScope.launch {
            repository.saveSeedColor(color)
        }
    }
}