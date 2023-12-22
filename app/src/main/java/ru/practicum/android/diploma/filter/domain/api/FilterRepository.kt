package ru.practicum.android.diploma.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.domain.models.Industry

interface FilterRepository {
    fun getIndustries(): Flow<NetworkResult<List<Industry>>>
    fun getCountries(): Flow<NetworkResult<List<Country>>>
    fun getAreas(id: String?): Flow<NetworkResult<List<Area>>>
    fun getCurrentFilter(): FilterParameters
    fun updateFilter(filter: FilterParameters)
}
