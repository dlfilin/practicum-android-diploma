package ru.practicum.android.diploma.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Industry

interface AddFilterRepository {

    suspend fun getIndustryAndSaveDb()
    suspend fun getCountryAndSaveDb()

    fun getIndustries(): Flow<List<Industry>>
    fun getCountries(): Flow<List<Country>>
}
