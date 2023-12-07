package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.network.dto.ErrorType
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchScreenState>()
    val state: LiveData<SearchScreenState> get() = _state

    private var latestSearchText: String? = null
    private val searchTrackDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_IN_MILLIS,
        viewModelScope,
        true
    ) { searchRequest ->
        searchRequest(searchRequest)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            this.latestSearchText = changedText
            searchTrackDebounce(latestSearchText!!)
        }
    }

    fun searchRequest(searchText: String) {
        if (searchText.isNotEmpty()) {
            renderState(SearchScreenState.Loading)

            viewModelScope.launch {
                searchInteractor
                    .searchVacancy(searchText)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundVacancyData: VacancyListData?, errorMessage: ErrorType?) {
        when {
            errorMessage != null -> {
                if (errorMessage == ErrorType.NO_INTERNET) {
                    renderState(SearchScreenState.InternetThrowable)
                } else {
                    renderState(SearchScreenState.Error)
                }
            }

            foundVacancyData?.items.isNullOrEmpty() -> {
                renderState(
                    SearchScreenState.Empty
                )
            }

            else -> {
                foundVacancyData?.let { renderState(SearchScreenState.Content(vacancyData = it)) }
            }
        }
    }

    private fun renderState(state: SearchScreenState) {
        _state.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_MILLIS = 2000L
    }
}
