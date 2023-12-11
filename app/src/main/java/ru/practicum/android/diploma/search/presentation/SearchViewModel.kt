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
import ru.practicum.android.diploma.search.domain.model.QuerySearch
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    val state: LiveData<SearchScreenState> get() = _state

    private val _filterState = MutableLiveData<FilterState>(FilterState(false))
    val filterState: LiveData<FilterState> get() = _filterState

    private var latestSearchText: String = ""
    private var filterParameters: FilterParameters = FilterParameters(
        null,
        null,
        null,
        false
    )
    private val searchTrackDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_IN_MILLIS,
        viewModelScope,
        true
    ) { searchRequest ->
        searchRequest(searchRequest)
    }

    private val searchTrackDebouncePraktikumPaging = debounce<QuerySearch>(
        SEARCH_DEBOUNCE_DELAY_IN_MILLIS,
        viewModelScope,
        true
    ) { searchRequest ->
        searchRequestPraktikumPaging(searchRequest)
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

    fun searchDebouncePraktikumPaging(search: QuerySearch) {
        if (search.text.isBlank()) {
            renderState(SearchScreenState.Default)
        }
        this.latestSearchText = search.text
        searchTrackDebouncePraktikumPaging(search)
    }

    fun checkFilterState() {
        filterParameters = searchInteractor.getFilterParameters()
        _filterState.postValue(FilterState(filterParameters.isNotEmpty))
    }

    private fun searchRequest(searchText: String) {
        if (searchText.isNotBlank()) {
            renderState(SearchScreenState.Loading)

            viewModelScope.launch {
                searchInteractor.searchVacancies(searchText, filterParameters).collect {
                    processResult(it)
                }
            }
        }
    }

    private fun searchRequestPraktikumPaging(search: QuerySearch) {
        if (search.text.isNotBlank()) {
            if (search.page == 0) renderState(SearchScreenState.Loading)

            viewModelScope.launch {
                searchInteractor.searchVacanciesPraktikumPaging(search, filterParameters).collect {
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
                    Log.e("DEBUG", result.data.page.toString())
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_MILLIS = 2000L
    }
}
