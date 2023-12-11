package ru.practicum.android.diploma.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.storage.FilterStorage
import ru.practicum.android.diploma.common.data.storage.mapper.FilterMapper
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.data.mapper.VacancyResponseMapper
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.search.domain.model.VacancyListData
import ru.practicum.android.diploma.vacancy.data.dto.SimilarVacancyRequest

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyMapper: VacancyResponseMapper,
    private val filterStorage: FilterStorage,
    private val filterMapper: FilterMapper
) : SearchRepository {

    override fun searchVacancies(text: String, filter: FilterParameters): Flow<Result<VacancyListData>> = flow {
        val result = networkClient.doRequest(
            VacancySearchRequest(prepareSearchQueryMap(text = text, filter = filter))
        )
        when (result) {
            is Result.Success -> {
                val data = vacancyMapper.mapDtoToModel(result.data as VacancySearchResponse)
                emit(Result.Success(data))
            }
            is Result.Error -> {
                emit(Result.Error(result.errorType!!))
            }
        }
    }

    override fun searchVacanciesPaged(text: String, filter: FilterParameters): Flow<PagingData<VacancyItem>> {
        val loader: VacancyPageLoader = { pageIndex, pageSize ->
            getVacancies(text, pageIndex, pageSize, filter)
        }
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { VacancyPagingSource(loader) }
        ).flow
    }

    private suspend fun getVacancies(text: String, pageIndex: Int, pageSize: Int, filter: FilterParameters): List<VacancyItem> {
        val result = networkClient.doRequest(
            VacancySearchRequest(prepareSearchQueryMap(text, pageIndex, pageSize, filter))
        )
        if (result is Result.Success) {
            return (result.data as VacancySearchResponse).items.map { it.toVacancyItem() }
        } else {
            throw Exception()
        }
    }

    override fun getSimilarVacancies(vacancyId: String): Flow<Result<VacancyListData>> = flow {
        val result = networkClient.doRequest(
            SimilarVacancyRequest(vacancyId)
        )
        when (result) {
            is Result.Success -> {
                val data = vacancyMapper.mapDtoToModel(result.data as VacancySearchResponse)
                emit(Result.Success(data))
            }
            is Result.Error -> {
                emit(Result.Error(result.errorType!!))
            }
        }
    }

    override fun getFilterParameters(): FilterParameters {
        return filterMapper.mapDtoToFilterParameters(filterStorage.getFilterParameters())
    }

    private fun prepareSearchQueryMap(text: String, pageIndex: Int = 0, pageSize: Int = 10, filter: FilterParameters): Map<String, String> {
        val map: HashMap<String, String> = HashMap()
        map["text"] = text
        map["page"] = pageIndex.toString()
        map["per_page"] = pageSize.toString()

        if (filter.area != null) {
            map["area"] = filter.area.id
        }
        if (filter.industry != null) {
            map["industry"] = filter.industry.id
        }
        if (filter.salary != null) {
            map["salary"] = filter.salary.toString()
        }
        if (filter.onlyWithSalary) {
            map["only_with_salary"] = "true"
        }
        return map
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
