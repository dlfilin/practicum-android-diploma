package ru.practicum.android.diploma.vacancy.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.common.data.network.dto.ErrorType
import ru.practicum.android.diploma.vacancy.domain.api.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.Phone
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyViewModel(
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {

    private var isFavorite = false

    private val vacancyState = MutableLiveData<VacancyScreenState>()
    fun observeVacancyState(): LiveData<VacancyScreenState> = vacancyState

    fun getVacancy(vacancyId: String) {
        vacancyState.postValue(VacancyScreenState.Loading)
        viewModelScope.launch {
            vacancyInteractor.getVacancy(vacancyId).collect {
                processResult(it.first, it.second)
            }
        }
    }

    fun makeCall(phone: Phone) {
        Log.d("DEBUG", "в другой задаче сделаем")
        TODO("make a call")
    }

    private fun processResult(vacancy: Vacancy?, errorMessge: ErrorType?) {
        when {
            errorMessge != null -> {
                vacancyState.postValue(VacancyScreenState.Error)
            }

            vacancy == null -> {
                vacancyState.postValue(VacancyScreenState.Error)
            }

            else -> {
                vacancyState.postValue(VacancyScreenState.Content(vacancy = vacancy))
            }
        }
    }

    /* fun toggleFavorite() {
         // пока просто для наглядности
         isFavorite = !isFavorite
         _state.postValue(VacancyScreenState(isFavorite))
     }*/
}
