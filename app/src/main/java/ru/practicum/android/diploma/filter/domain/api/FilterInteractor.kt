package ru.practicum.android.diploma.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.models.Industry

interface FilterInteractor {

    suspend fun getIndustryAndSaveDb()

    fun getIndustries(): Flow<List<Industry>>
}
