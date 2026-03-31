package com.markix.gavclient.logic.viewmodels.programming

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.markix.gavclient.logic.data.user.ClassroomInfo
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class PGHomeData(
    var schoolYears: MutableList<List<String>> = mutableListOf()
)

class PGHomeViewModel(application : Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(PGHomeData())
    val uiState: StateFlow<PGHomeData> = _uiState.asStateFlow()

    suspend fun getSchoolYears(gaVM: GAVAPIViewModel) {
        val accInf: ClassroomInfo = gaVM.getClassroomInfo();
        // this is objectively a HORRIBLE solution, but it will *technically* work until the year 2100
        // (and god knows if the school is going to be around by then)
        val baseYear: Int = Integer.parseInt("20" + accInf.classroom!!.substring(1, 3));
        val currentYear = LocalDate.now().year;

        val semesterYears = (baseYear..currentYear).toList().toIntArray()
        val semesterPairs: MutableList<List<String>> = mutableListOf()
        var i = 0;
        while (i < semesterYears.size) {
            if (i != semesterYears.size - 1) {
                semesterPairs.add(listOf(semesterYears.get(i).toString(), semesterYears.get(i + 1).toString()))
            }
            i++;
        }

        _uiState.update { currentState ->
            currentState.copy(
                schoolYears = semesterPairs
            )
        };
    }
}