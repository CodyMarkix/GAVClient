package com.markix.gavclient.logic.viewmodels.ioc

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.markix.gavclient.logic.data.ioc.IOCAgendaData
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
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