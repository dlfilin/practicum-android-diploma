package ru.practicum.android.diploma.vacancy.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.vacancy.domain.api.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyViewModel(
    private val vacancyId: String,
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {

    private var isFavorite = false
    private val vacancyState = MutableLiveData<VacancyScreenState>()
    fun observeVacancyState(): LiveData<VacancyScreenState> = vacancyState

    fun getVacancy(vacancyId: String) {
        vacancyState.postValue(VacancyScreenState.Loading)
        viewModelScope.launch {
            vacancyInteractor.getVacancy(vacancyId).collect {
                processResult(it)
            }
        }
    }

    fun makeCall(phone: Phone) {
        Log.d("DEBUG", "в другой задаче сделаем")
        TODO("make a call")
    }

    private fun processResult(result: Result<Vacancy>) {
        when (result) {
            is Result.Success -> {
                renderState(VacancyScreenState.Content(result.data!!))
            }

            is Result.Error -> {
                if (result.errorType == ErrorType.NO_INTERNET) {
                    renderState(VacancyScreenState.InternetThrowable)
                } else {
                    renderState(VacancyScreenState.Error)
                }
            }
        }
    }

    private fun renderState(state: VacancyScreenState) {
        vacancyState.postValue(state)
    }

    fun toggleFavorite() {
        // пока просто для наглядности
        isFavorite = !isFavorite
//         vacancyState.postValue(VacancyScreenState(isFavorite))
    }

    fun getVacancyId(): String = vacancyId
}
