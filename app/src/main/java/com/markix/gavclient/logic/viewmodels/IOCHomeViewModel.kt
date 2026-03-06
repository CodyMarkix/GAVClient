package com.markix.gavclient.logic.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.currentRecomposeScope
import androidx.lifecycle.AndroidViewModel
import com.markix.gavclient.logic.data.IOCAgendaData
import com.markix.gavclient.ui.apps.ioc.IOCHome
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class IOCHomeData(
    var agendas: List<IOCAgendaData> = emptyList()
)

class IOCHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(IOCHomeData())
    val uiState: StateFlow<IOCHomeData> = _uiState.asStateFlow()

    suspend fun getIOCAgendaList(authViewModel: GAVAPIViewModel) {
        val agendaData = authViewModel.getIOCAgendas()
        _uiState.update { currentState ->
            currentState.copy(
                agendas = agendaData
            )
        }
        Log.d("com.markix.gavclient", agendaData.toString())
    }
}