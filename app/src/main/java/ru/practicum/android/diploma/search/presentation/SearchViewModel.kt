package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    val state: LiveData<SearchScreenState> get() = _state

    private val _filterState = MutableLiveData<FilterParameters>(FilterParameters())
    val filterState: LiveData<FilterParameters> get() = _filterState

    private var latestSearchText: String = ""
    private val searchTrackDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_IN_MILLIS,
        viewModelScope,
        true
    ) { searchRequest ->
        searchRequest(searchRequest)
    }

    init {
        checkIfFilterWasUpdated()
    }

    fun searchDebounce(changedText: String) {
        if (changedText.isBlank()) {
            renderState(SearchScreenState.Default)
        }
        if (latestSearchText != changedText) {
            this.latestSearchText = changedText
            searchTrackDebounce(latestSearchText)
        }
    }

    private fun searchRequest(searchText: String) {
        if (searchText.isNotBlank()) {
            renderState(SearchScreenState.Loading)

            viewModelScope.launch {
                searchInteractor.searchVacancies(searchText, _filterState.value!!).collect {
                    processResult(it)
                }
            }
        }
    }

    private fun processResult(result: Result<VacancyListData>) {
        when (result) {
            is Result.Success -> {
                if (result.data?.items.isNullOrEmpty()) {
                    renderState(SearchScreenState.Empty)
                } else {
                    renderState(SearchScreenState.Content(result.data!!))
                    Log.e("SIZE", result.data.items.size.toString())
                    Log.e("DATA", result.data.items.toString())
                }
            }

            is Result.Error -> {
                if (result.errorType == ErrorType.NO_INTERNET) {
                    renderState(SearchScreenState.InternetThrowable)
                } else {
                    renderState(SearchScreenState.Error)
                }
            }
        }
    }

    private fun renderState(state: SearchScreenState) {
        _state.postValue(state)
    }

    fun isFilterActive(): Boolean {
        return _filterState.value!!.isNotEmpty
    }

    fun checkIfFilterWasUpdated() {
        val oldFilter = _filterState.value!!
        // пока тест, потом полезем в sharedprefs
        val newFilter = FilterParameters(onlyWithSalary = false)

        // проверяем, если он изменился
        if (oldFilter != newFilter) {
            _filterState.postValue(newFilter)
            searchRequest(latestSearchText)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_MILLIS = 2000L
    }
}
