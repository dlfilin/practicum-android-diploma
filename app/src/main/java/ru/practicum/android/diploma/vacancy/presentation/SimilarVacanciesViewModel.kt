package ru.practicum.android.diploma.vacancy.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SimilarVacanciesViewModel(
    private val vacancyId: String,
    private val searchInteractor: SearchInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SimilarVacanciesScreenState>(SimilarVacanciesScreenState.Empty)
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

    private fun processResult(result: Result<VacancyListData>) {
        when (result) {
            is Result.Success -> {
                if (result.data?.items.isNullOrEmpty()) {
                    renderState(SimilarVacanciesScreenState.Empty)
                } else {
                    renderState(SimilarVacanciesScreenState.Content(result.data!!))
                    Log.e("SIZE", result.data.items.size.toString())
                    Log.e("DATA", result.data.items.toString())
                }
            }

            is Result.Error -> {
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
