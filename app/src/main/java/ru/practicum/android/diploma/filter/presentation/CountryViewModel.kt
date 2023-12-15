package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Country

class CountryViewModel(private val interactor: FilterInteractor) : ViewModel() {

    init {
        viewModelScope.launch {
            interactor.getCountryAndSaveDb()
        }
    }

    fun getCountries(): Flow<List<Country>> = interactor.getCountries()

    fun saveCountry(country: Country) {
        interactor.saveCountry(country)
    }
}
