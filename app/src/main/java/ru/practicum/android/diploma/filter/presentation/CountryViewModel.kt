package ru.practicum.android.diploma.filter.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Country

class CountryViewModel(private val interactor: FilterInteractor) : ViewModel() {

    fun getCountries(): Flow<List<Country>> = interactor.getCountries()
}
