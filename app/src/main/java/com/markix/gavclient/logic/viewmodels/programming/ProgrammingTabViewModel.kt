package com.markix.gavclient.logic.viewmodels.programming

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProgrammingTabData(
    var balls: Int = 0
)

class ProgrammingTabViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ProgrammingTabData())
    val uiState: StateFlow<ProgrammingTabData> = _uiState.asStateFlow()


}