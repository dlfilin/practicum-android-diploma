package ru.practicum.android.diploma.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.domain.models.Industry

interface FilterRepository {

    suspend fun getIndustryAndSaveDb()
    suspend fun getCountryAndSaveDb()
    suspend fun getAreaAndSaveDb()

    fun getIndustries(): Flow<List<Industry>>
    fun getCountries(): Flow<List<Country>>
    fun getAreas(): Flow<List<Area>>

    fun checkApplyBtnVisible(): Boolean
    fun getEditableFilter(): FilterParameters
    fun updateEditableFilter(filter: FilterParameters)

    fun saveIndustry(industry: Industry)
    fun saveCountry(country: Country)
    fun saveArea(area: Area)

    fun saveEditableInMainFilter()
    fun saveMainInEditableFilter()
}
