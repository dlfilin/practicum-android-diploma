package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.network.dto.Resource
import ru.practicum.android.diploma.search.domain.model.VacancyListData

interface SearchRepository {

    fun searchVacancy(text: String): Flow<Resource<VacancyListData>>

}
