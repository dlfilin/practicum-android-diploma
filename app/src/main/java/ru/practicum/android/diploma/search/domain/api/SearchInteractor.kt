package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.search.domain.model.QuerySearch
import ru.practicum.android.diploma.search.domain.model.VacancyListData

interface SearchInteractor {
    fun searchVacancies(querySearch: QuerySearch, options: FilterParameters): Flow<Result<VacancyListData>>
    fun getSimilarVacancies(vacancyId: String): Flow<NetworkResult<VacancyListData>>
    fun getFilterParameters(): FilterParameters
}
