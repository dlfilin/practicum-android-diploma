package ru.practicum.android.diploma.search.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.data.network.dto.ErrorType
import ru.practicum.android.diploma.search.domain.model.VacancyListData

interface SearchInteractor {
    fun searchVacancy(text: String): Flow<Pair<VacancyListData?, ErrorType?>>
}
