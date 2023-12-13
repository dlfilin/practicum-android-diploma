package ru.practicum.android.diploma.search.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.data.mapper.VacancyResponseMapper
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.model.SearchQuery
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyMapper: VacancyResponseMapper,
) : SearchRepository {

    override fun searchVacanciesPaged(
        searchQuery: SearchQuery,
        filter: FilterParameters
    ): Flow<NetworkResult<VacancyListData>> = flow {
        val request = VacancySearchRequest(prepareSearchQueryMap(searchQuery))
        when (val result = networkClient.doRequest(request)) {
            is NetworkResult.Success -> {
                val data = vacancyMapper.mapDtoToModel(result.data as VacancySearchResponse)
                emit(NetworkResult.Success(data))
            }

            is NetworkResult.Error -> {
                emit(NetworkResult.Error(result.errorType!!))
            }
        }
    }

    private fun prepareSearchQueryMap(
        searchQuery: SearchQuery
    ): Map<String, String> {
        val map: HashMap<String, String> = HashMap()
        map["text"] = searchQuery.text
        map["page"] = searchQuery.page.toString()
        map["per_page"] = searchQuery.perPage.toString()

        return map
    }
}
