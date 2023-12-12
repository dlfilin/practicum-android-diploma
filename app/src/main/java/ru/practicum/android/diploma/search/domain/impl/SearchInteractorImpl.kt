package ru.practicum.android.diploma.search.domain.impl

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    override fun searchVacancies(text: String, options: FilterParameters): Flow<Result<VacancyListData>> {
        return repository.searchVacancies(text, options)
    }

    override fun getSimilarVacancies(vacancyId: String): Flow<Result<VacancyListData>> {
        return repository.getSimilarVacancies(vacancyId)
    }

    override fun getFilterParameters(): FilterParameters {
        return repository.getFilterParameters()
    }

    override fun searchVacanciesPaged(text: String, filter: FilterParameters): Flow<PagingData<VacancyItem>> {
        return repository.searchVacanciesPaged(text, filter)
    }
}
