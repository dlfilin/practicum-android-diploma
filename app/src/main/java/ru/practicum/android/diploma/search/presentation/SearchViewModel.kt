package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.search.domain.model.VacancyListData

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
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

    val itemsFlow: Flow<PagingData<VacancyItem>>

    private val searchBy = MutableLiveData("")

    init {
        itemsFlow = searchBy.asFlow()
            // if user types text too quickly -> filtering intermediate values to avoid excess loads
            .debounce(2000)
            .flatMapLatest {
                searchInteractor.searchVacanciesPaged(it, filterParameters)
            }
            // always use cacheIn operator for flows returned by Pager. Otherwise exception may be thrown
            // when 1) refreshing/invalidating or 2) subscribing to the flow more than once.
            .cachedIn(viewModelScope)
    }

    fun setSearchBy(value: String) {
        if (this.searchBy.value == value || value.isBlank()) return
        this.searchBy.value = value
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

    fun checkFilterState() {
        filterParameters = searchInteractor.getFilterParameters()
        _filterState.postValue(FilterState(filterParameters.isNotEmpty))
    }

    private fun searchRequest(searchText: String) {
        if (searchText.isNotBlank()) {
            renderState(SearchScreenState.Loading)

            viewModelScope.launch {
                searchInteractor.searchVacanciesPaged(searchText, filterParameters).collect {
//                    processResult(it)
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_MILLIS = 2000L
    }
}
