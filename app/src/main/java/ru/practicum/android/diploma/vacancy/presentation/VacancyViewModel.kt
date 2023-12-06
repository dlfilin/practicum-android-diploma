package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VacancyViewModel : ViewModel() {

    private var isFavorite = false

    private val _state = MutableLiveData(VacancyScreenState(false))
    val state: LiveData<VacancyScreenState> get() = _state

    fun toggleFavorite() {
        // пока просто для наглядности
        isFavorite = !isFavorite
        _state.postValue(VacancyScreenState(isFavorite))
    }
}
