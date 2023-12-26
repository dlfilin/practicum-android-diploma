package ru.practicum.android.diploma.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.common.util.SingleLiveEvent
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.model.SearchQuery
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SimilarVacanciesViewModel(
    private val vacancyId: String,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SimilarVacanciesScreenState>()
    val state: LiveData<SimilarVacanciesScreenState> get() = _state

    private val _toastEvent = SingleLiveEvent<ErrorType>()
    val toastEvent: LiveData<ErrorType> get() = _toastEvent

    private var currentPage: Int = 0
    private var maxPages = 1
    private var totalFound: Int = 0
    private var isNextPageLoading: Boolean = false
    private var vacanciesList = mutableListOf<VacancyItem>()

    init {
        getSimilarVacancies()
    }

    private fun getSimilarVacancies() {
        clearPagingInfo()
        renderState(SimilarVacanciesScreenState.Loading)
        searchRequest()
    }

    private fun searchRequest() {
        viewModelScope.launch {
            searchInteractor.getSimilarVacanciesPaged(
                vacancyId = vacancyId,
                SearchQuery(
                    page = currentPage,
                    perPage = ITEMS_PER_PAGE
                )
            ).collect {
                processResult(it)
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
                    renderState(SimilarVacanciesScreenState.Empty)
                } else {
                    vacanciesList.addAll(vacancyListData.items)
                    val data = vacancyListData.copy(items = vacanciesList)
                    renderState(SimilarVacanciesScreenState.Content(vacancyData = data, isLoading = false))
                }
            }

            is NetworkResult.Error -> {
                if (currentPage > 0) {
                    _toastEvent.postValue(result.errorType!!)
                    val state = (state.value as SimilarVacanciesScreenState.Content).copy(isLoading = false)
                    renderState(state)
                } else {
                    renderState(SimilarVacanciesScreenState.Error(result.errorType!!))
                }
            }
        }
    }

    private fun renderState(state: SimilarVacanciesScreenState) {
        _state.postValue(state)
    }

    fun onLastItemReached() {
        if (currentPage < maxPages - 1 && !isNextPageLoading) {
            val state = (state.value as SimilarVacanciesScreenState.Content).copy(isLoading = true)
            renderState(state)
            isNextPageLoading = true
            currentPage++
            searchRequest()
        }
    }

    private fun clearPagingInfo() {
        currentPage = 0
        totalFound = 0
        maxPages = 1
        vacanciesList = mutableListOf()
    }

    companion object {
        private const val ITEMS_PER_PAGE = 20
    }
}
