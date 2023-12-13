package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.AddFilterRepository
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Industry

class FilterInteractorImpl(private val repository: AddFilterRepository) : FilterInteractor {

    override suspend fun getIndustryAndSaveDb() {
        repository.getIndustryAndSaveDb()
    }

    override suspend fun getCountryAndSaveDb() {
        repository.getCountryAndSaveDb()
    }

    override fun getIndustries(): Flow<List<Industry>> = repository.getIndustries()
    override fun getCountries(): Flow<List<Country>> = repository.getCountries()
}
