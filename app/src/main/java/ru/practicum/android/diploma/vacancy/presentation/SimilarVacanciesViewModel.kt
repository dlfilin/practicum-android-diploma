package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SimilarVacanciesViewModel(
    private val vacancyId: String,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SimilarVacanciesScreenState>()
    val state: LiveData<SimilarVacanciesScreenState> get() = _state

    init {
        getSimilarVacancies()
    }

    private fun getSimilarVacancies() {
        renderState(SimilarVacanciesScreenState.Loading)

        viewModelScope.launch {
            searchInteractor.getSimilarVacancies(vacancyId).collect {
                processResult(it)
            }
        }
    }

    private fun processResult(result: NetworkResult<VacancyListData>) {
        when (result) {
            is NetworkResult.Success -> {
                if (result.data?.items.isNullOrEmpty()) {
                    renderState(SimilarVacanciesScreenState.Empty)
                } else {
                    renderState(SimilarVacanciesScreenState.Content(result.data!!))
                }
            }

            is NetworkResult.Error -> {
                if (result.errorType == ErrorType.NO_INTERNET) {
                    renderState(SimilarVacanciesScreenState.InternetThrowable)
                } else {
                    renderState(SimilarVacanciesScreenState.Error)
                }
            }
        }
    }

    private fun renderState(state: SimilarVacanciesScreenState) {
        _state.postValue(state)
    }
}
