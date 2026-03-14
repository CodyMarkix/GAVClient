package com.markix.gavclient.logic.viewmodels.seminars

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SeminarsHomeData(
    var selectedTab: Array<Boolean> = arrayOf(true, false)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SeminarsHomeData

        if (!selectedTab.contentEquals(other.selectedTab)) return false

        return true
    }

    override fun hashCode(): Int {
        return selectedTab.contentHashCode()
    }
}

class SeminarsHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(SeminarsHomeData())
    val uiState: StateFlow<SeminarsHomeData> = _uiState.asStateFlow()


}