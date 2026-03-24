package com.markix.gavclient.logic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class APIOutputData (
    var apiOutput: String = "",
)

class DebugAPIViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(APIOutputData())
    val uiState: StateFlow<APIOutputData> = _uiState.asStateFlow()

    suspend fun callAPI(query: String, gaVM: GAVAPIViewModel) {
        val output = gaVM.debugApiCall(query)
        _uiState.update { currentState ->
            currentState.copy(
                apiOutput = output
            )
        }
    }
}
