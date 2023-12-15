package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
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

    override fun getAreas(): Flow<List<Area>> = repository.getAreas()
    override fun checkApplyBtnVisible(): Boolean {
        return repository.checkApplyBtnVisible()
    }

    override fun getIndustries(): Flow<List<Industry>> = repository.getIndustries()
    override fun getCountries(): Flow<List<Country>> = repository.getCountries()

    override fun getEditableFilter(): FilterParameters {
        return repository.getEditableFilter()
    }

    override fun updateEditableFilter(filter: FilterParameters) {
        repository.updateEditableFilter(filter)
    }

    override fun saveIndustry(industry: Industry) {
        repository.saveIndustry(industry)
    }

    override fun saveCountry(country: Country) {
        repository.saveCountry(country)
    }

    override fun saveArea(area: Area) {
        repository.saveArea(area)
    }

    override fun saveEditableInMainFilter() {
        repository.saveEditableInMainFilter()
    }

    override fun saveMainInEditableFilter() {
        repository.saveMainInEditableFilter()
    }
}
