package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor

class FilterViewModel(private val interactor: FilterInteractor) : ViewModel() {

    suspend fun getIndustryAndSaveDb() {
        interactor.getIndustryAndSaveDb()
    }

    suspend fun getCountryAndSaveDb() {
        interactor.getCountryAndSaveDb()
    }

}
