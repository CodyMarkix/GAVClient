package com.markix.gavclient.logic.viewmodels.ioc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.markix.gavclient.logic.data.IOCKeyword
import com.markix.gavclient.logic.data.IOCTopic
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class IOCAgendaData(
    var topics: List<IOCTopic> = emptyList<IOCTopic>(),
    var keywords: List<IOCKeyword> = emptyList<IOCKeyword>(),
    var selectedFilters: List<Int> = emptyList<Int>().toMutableList(),
    var selectedTopics: List<IOCTopic> = emptyList<IOCTopic>().toMutableList()
)

class IOCAgendaViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(IOCAgendaData())
    val uiState: StateFlow<IOCAgendaData> = _uiState.asStateFlow()

    suspend fun getAgendaKeywords(authViewModel: GAVAPIViewModel, agendaId: Int) {
        val keywordList = authViewModel.getIOCAgendaKeywords(agendaId)
        _uiState.update { currentState ->
            currentState.copy(
                keywords = keywordList
            )
        }
    }
    suspend fun getTopicList(authViewModel: GAVAPIViewModel, id: Int) {
        val topicListNoId = authViewModel.getIOCTopics(id)
        val topicList = emptyList<IOCTopic>().toMutableList()
        val topicKeyWords = authViewModel.getIOCTopicKeywords(id)

        for (n in topicListNoId) {
            val nWithId = IOCTopic(
                id = n.id,
                title = n.title,
                description = n.description,
                keywords = topicKeyWords
            )
            topicList += nWithId
        }

        _uiState.update { currentState ->
            currentState.copy(
                topics = topicList
            )
        }
    }

    fun refreshTopicList() {
        var newSelectedList: List<IOCTopic> = emptyList<IOCTopic>().toMutableList();

        if (_uiState.value.selectedFilters.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedTopics = _uiState.value.topics.toMutableList()
                )
            }
        } else {
            _uiState.value.topics.forEach { topic ->
                if (topic.keywords!!.any(_uiState.value.selectedFilters::contains)) {
                    newSelectedList.plus(topic)
                }
            }

            _uiState.update{ currentState ->
                currentState.copy(
                    selectedTopics = newSelectedList.toMutableList() // Redundancy to prevent reference by pointer
                )
            }
        }
    }
}