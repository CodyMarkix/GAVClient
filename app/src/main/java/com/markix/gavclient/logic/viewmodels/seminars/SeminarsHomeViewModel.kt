package com.markix.gavclient.logic.viewmodels.seminars

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.markix.gavclient.logic.data.SeminarData
import com.markix.gavclient.logic.data.SeminarSlot
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SeminarsHomeData(
    var slots: List<SeminarSlot> = listOf(),
    var mySeminars: List<SeminarData> = listOf(),
    var availableSeminars: MutableList<List<SeminarData>> = mutableListOf(),
    var selectedSeminarTabIndex: Int = 0
)

class SeminarsHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(SeminarsHomeData())
    val uiState: StateFlow<SeminarsHomeData> = _uiState.asStateFlow()

    suspend fun fetchSlots(gaVM: GAVAPIViewModel) {
        val currentSlots = gaVM.getSeminarSlots()
        Log.d("com.markix.gavclient", currentSlots.toString())
        _uiState.update { currentState ->
            currentState.copy(
                slots = currentSlots
            )
        }
    }

    fun findSlotById(id: Int): SeminarSlot {
        Log.d("com.markix.gavclient", _uiState.value.slots.toString())
        for (slot in _uiState.value.slots) {
            if (slot.id!! == id) {
                // Returning an index instead of the slot itself so that it doesn't screw with
                // any recomposition/state handling Jetpack might be doing
                return slot
            }
        }

        return SeminarSlot(0, "") // Arbitrarily chosen number to say "Hey buddy, something went wrong"
    }

    suspend fun getUserSeminars(gaVM: GAVAPIViewModel) {
        Log.d("com.markix.gavclient", "Calling gaVM.getUserSeminars()")
        val seminars: List<SeminarData> = gaVM.getUserSeminars()
        _uiState.update { currentState ->
            currentState.copy(
                mySeminars = seminars
            )
        }
    }

    suspend fun getAvailableSeminars(gaVM: GAVAPIViewModel) {
        while (_uiState.value.slots.count() == 0) {
            continue
        }
        var newAvailableSeminars: MutableList<List<SeminarData>> = mutableListOf()

        for (slot in _uiState.value.slots) {
            val seminars: List<SeminarData> = gaVM.getAvailableSeminars(slot.id ?: 0)
            newAvailableSeminars.add(seminars)
        }

        _uiState.update { currentState ->
            currentState.copy(
                availableSeminars = newAvailableSeminars
            )
        }
    }

    fun convertLectorsStringToList(lectors: String): List<String> {
        return lectors.split(",\\s+(?=(?:Ing\\.|doc\\.|RNDr\\.|prof\\.|MUDr\\.))")
    }
}