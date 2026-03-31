package com.markix.gavclient.logic.viewmodels.programming

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.markix.gavclient.logic.data.programming.ProgrammingAssignmentData
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

data class PGSchoolYearData(
    var assignments: MutableList<MutableList<ProgrammingAssignmentData>> = mutableListOf()
)

class PGSchoolYearViewModel(application : Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(PGSchoolYearData())
    val uiState: StateFlow<PGSchoolYearData> = _uiState.asStateFlow()

    fun convertPointsToGrade(points: Int, max: Double, min: Double): Int {
        when {
            points.toDouble() > max -> {
                return 1
            }

            points.toDouble() in (max * 0.86)..max -> {
                return 1
            }

            points.toDouble() in (max * 0.71)..(max * 0.85) -> {
                return 2
            }

            points.toDouble() in (max * 0.56)..(max * 0.70) -> {
                return 3
            }

            points.toDouble() in (max * 0.40)..(max * 0.55) -> {
                return 4
            }

            points.toDouble() in (max * 0)..(max * 0.40) -> {
                return 5
            }

            else -> {
                return 0
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun fetchAssignments(gaVM: GAVAPIViewModel, schoolYear: List<String>) {
        val allTasks: List<ProgrammingAssignmentData> = gaVM.getClassroomTasks()
        var s1: MutableList<ProgrammingAssignmentData> = mutableListOf()
        var s2: MutableList<ProgrammingAssignmentData> = mutableListOf()
        var categorizedList: MutableList<MutableList<ProgrammingAssignmentData>> = mutableListOf()

        var i = 0
        while (i < allTasks.size) {
            val taskDate = allTasks[i].validto?.toLocalDateTime(TimeZone.currentSystemDefault())?.date
            if (taskDate?.year.toString() == schoolYear[0]) {
                when {
                    ((taskDate?.month?.ordinal ?: 0) + 1) in 9..12 -> {
                        s1.add(allTasks[i])
                    }

                    ((taskDate?.month?.ordinal ?: 0) + 1) == 1 -> {
                        s1.add(allTasks[i])
                    }
                }
            } else if (taskDate?.year.toString() == schoolYear[1]) {
                when {
                    ((taskDate?.month?.ordinal ?: 5) + 1) in 2..6 -> {
                        s2.add(allTasks[i])
                    }
                }
            }

            i++;
        }

        categorizedList.add(s1)
        categorizedList.add(s2)

        _uiState.update { currentState ->
            currentState.copy(
                assignments = categorizedList
            )
        }
    }
}