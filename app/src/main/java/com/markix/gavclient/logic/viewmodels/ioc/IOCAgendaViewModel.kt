package com.markix.gavclient.logic.viewmodels.ioc

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.markix.gavclient.logic.data.ioc.IOCKeyword
import com.markix.gavclient.logic.data.ioc.IOCTopic
import com.markix.gavclient.logic.viewmodels.GAVAPIViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.yield

data class IOCAgendaUIData(
    var topics: List<IOCTopic> = emptyList<IOCTopic>(),
    var keywords: List<IOCKeyword> = emptyList<IOCKeyword>(),
    var filter: List<Int> = emptyList<Int>().toMutableList(),
    var selectedTopics: List<IOCTopic> = emptyList<IOCTopic>().toMutableList(),
    var loadingTopics: Boolean = true
)

class IOCAgendaViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(IOCAgendaUIData())
    val uiState: StateFlow<IOCAgendaUIData> = _uiState.asStateFlow()

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

        for (n in topicListNoId) {
            val topicKeyWords = authViewModel.getIOCTopicKeywords(n.id ?: 0)

            val nWithId = IOCTopic(
                id = n.id,
                title = n.title,
                description = n.description,
                keywords = topicKeyWords
            )
            topicList.add(nWithId)
        }

        _uiState.update { currentState ->
            currentState.copy(
                topics = topicList,
                loadingTopics = false
            )
        }
    }

    fun refreshTopicList() {
        var newSelectedList: List<IOCTopic> = emptyList<IOCTopic>().toMutableList();

        if (_uiState.value.filter.isEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(
                    selectedTopics = _uiState.value.topics.toMutableList()
                )
            }
        } else {
            _uiState.value.topics.forEach { topic ->
                if (topic.keywords!!.any(_uiState.value.filter::contains)) {
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

    fun selectKeywordFilter(keyword: Int) {
        val newFilter = _uiState.value.filter.toMutableList()

        newFilter.add(keyword)
        _uiState.update { currentState ->
            currentState.copy(
                filter = newFilter.toList()
            )
        }
    }

    fun removeKeywordFilter(keyword: Int) {
        val newFilter = _uiState.value.filter.toMutableList()

        newFilter.remove(keyword)
        _uiState.update { currentState ->
            currentState.copy(
                filter = newFilter.toList()
            )
        }
    }
}