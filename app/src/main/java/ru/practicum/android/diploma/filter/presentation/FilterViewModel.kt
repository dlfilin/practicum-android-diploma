package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor

class FilterViewModel(private val interactor: FilterInteractor) : ViewModel() {

    fun getIndustryAndSaveDb() {
        viewModelScope.launch {
            interactor.getIndustryAndSaveDb()
        }
    }

    fun getCountryAndSaveDb() {
        viewModelScope.launch {
            interactor.getCountryAndSaveDb()
        }
    }

    fun getAreaAndSaveDb() {
        viewModelScope.launch {
            interactor.getAreaAndSaveDb()
        }
    }

}
