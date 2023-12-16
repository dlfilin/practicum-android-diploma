package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.domain.models.Industry

class FilterInteractorImpl(private val repository: FilterRepository) : FilterInteractor {

    override suspend fun getIndustryAndSaveDb() {
        repository.getIndustryAndSaveDb()
    }

    override suspend fun getCountryAndSaveDb() {
        repository.getCountryAndSaveDb()
    }

    override suspend fun getAreaAndSaveDb() {
        repository.getAreaAndSaveDb()
    }

    override fun getAreas(): Flow<NetworkResult<List<Area>>> = repository.getAreas()
    override fun getIndustries(): Flow<NetworkResult<List<Industry>>> = repository.getIndustries()
    override fun getCountries(): Flow<NetworkResult<List<Country>>> = repository.getCountries()

    override fun getCurrentFilter(): FilterParameters {
        return repository.getCurrentFilter()
    }

    override fun updateFilter(filter: FilterParameters) {
        repository.updateFilter(filter)
    }
}
