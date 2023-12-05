package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyViewModel : ViewModel() {

    private val _state = MutableLiveData(VacancyScreenState())
    val state: LiveData<VacancyScreenState> get() = _state

    fun isVacancyFavorite() = state.value?.vacancy?.isFavorite ?: false

    fun toggleFavorite() {
        // пока просто для наглядности
        val update = state.value?.vacancy?.let {
            it.copy(isFavorite = !it.isFavorite)
        } ?: Vacancy()
        _state.postValue(VacancyScreenState(update))
    }

}
