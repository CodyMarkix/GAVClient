package com.markix.gavclient.logic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.markix.gavclient.logic.data.ClassroomInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Date

data class PGHomeData(
    var schoolYears: MutableList<String> = emptyList<String>().toMutableList()
)

class PGHomeViewModel(application : Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(PGHomeData())
    val uiState: StateFlow<PGHomeData> = _uiState.asStateFlow()

    suspend fun getSchoolYears(gaVM: GAVAPIViewModel) {
        val accInf: ClassroomInfo = gaVM.getClassroomInfo();
        val baseYear: Int = Integer.parseInt("20" + accInf.classroom!!.substring(1, 3)); // this is objectively a HORRIBLE solution, but it will *technically* work until the year 2100 (and God knows if the school is going to be around by then)
        val currentYear = LocalDate.now().year;
        var i = 0;
        while (i < (baseYear - currentYear)) {
            val yearsToAdd = (currentYear..baseYear).map { it.toString() }.toMutableList()

            _uiState.update { currentState ->
                currentState.copy(
                    schoolYears = yearsToAdd
                )
            }
            i++;
        }
    }
}