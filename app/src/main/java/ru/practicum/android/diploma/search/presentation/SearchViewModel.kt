package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.common.util.SingleLiveEvent
import ru.practicum.android.diploma.common.util.debounce
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.model.SearchQuery
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SearchViewModel(
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchScreenState>(SearchScreenState.Default)
    val state: LiveData<SearchScreenState> get() = _state

    private val _isFilterActiveState = MutableLiveData(false)
    val isFilterActiveState: LiveData<Boolean> get() = _isFilterActiveState

    private val _toastEvent = SingleLiveEvent<ErrorType>()
    val toastEvent: LiveData<ErrorType> get() = _toastEvent

    private var currentPage: Int = 0
    private var maxPages = 1
    private var totalFound: Int = 0
    private var isNextPageLoading: Boolean = false
    private var vacanciesList = mutableListOf<VacancyItem>()

    private var latestSearchText: String = ""

    private val searchDebounced = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_IN_MILLIS,
        viewModelScope,
        true
    ) { searchText ->
        if (searchText != latestSearchText) {
            clearPagingInfo()
            latestSearchText = searchText
            renderState(SearchScreenState.InitialLoading)
            searchRequest(currentPage)
        }
    }

    fun searchTextChanged(changedText: String) {
        if (changedText.isBlank()) {
            clearPagingInfo()
            latestSearchText = ""
            renderState(SearchScreenState.Default)
        }
        searchDebounced(changedText)
    }

    fun onLastItemReached() {
        if (currentPage < maxPages - 1 && !isNextPageLoading) {
            renderState(SearchScreenState.NextPageLoading(true))
            isNextPageLoading = true
            searchRequest(currentPage + 1)
        }
    }


    fun applyFilter() {
        clearPagingInfo()
        searchRequest(currentPage)
    }

    private fun searchRequest(page: Int) {
        viewModelScope.launch {
            searchInteractor.searchVacanciesPaged(
                SearchQuery(
                    text = latestSearchText,
                    page = page,
                    perPage = ITEMS_PER_PAGE
                )
            ).collect {
                processResult(result = it)
                isNextPageLoading = false
            }
        }
    }

    private fun processResult(result: NetworkResult<VacancyListData>) {
        when (result) {
            is NetworkResult.Success -> {
                val vacancyListData = result.data!!
                with(vacancyListData) {
                    maxPages = pages
                    totalFound = found
                    currentPage = page
                }
                if (totalFound == 0) {
                    renderState(SearchScreenState.Empty)
                } else {
                    vacanciesList.addAll(vacancyListData.items)
                    val data = vacancyListData.copy(items = vacanciesList)
                    renderState(SearchScreenState.Content(data))
                }
            }

            is NetworkResult.Error -> {
                if (currentPage > 0) {
                    _toastEvent.postValue(result.errorType!!)
                    renderState(SearchScreenState.NextPageLoading(false))
                } else {
                    renderState(SearchScreenState.Error(result.errorType!!))
                }
            }
        }
    }

    private fun renderState(state: SearchScreenState) {
        _state.postValue(state)
    }

    private fun clearPagingInfo() {
        currentPage = 0
        totalFound = 0
        maxPages = 1
        vacanciesList = mutableListOf()
    }

    fun checkFilterState() {
        val isActive = searchInteractor.isFilterActive()
        _isFilterActiveState.postValue(isActive)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_MILLIS = 2000L
        private const val ITEMS_PER_PAGE = 20
    }
}
