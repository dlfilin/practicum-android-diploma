package ru.practicum.android.diploma.search.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.CONTENT
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.NO_INTERNET
import ru.practicum.android.diploma.common.data.storage.FilterStorage
import ru.practicum.android.diploma.common.data.storage.mapper.FilterMapper
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.data.mapper.VacancyResponseMapper
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.model.VacancyListData
import ru.practicum.android.diploma.vacancy.data.dto.SimilarVacancyRequest

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyMapper: VacancyResponseMapper,
    private val filterStorage: FilterStorage,
    private val filterMapper: FilterMapper
) : SearchRepository {

    override fun searchVacancies(text: String, filter: FilterParameters): Flow<Result<VacancyListData>> = flow {
        val response = networkClient.doRequest(
            VacancySearchRequest(prepareSearchQueryMap(text, filter))
        )
        when (response.resultCode) {
            NO_INTERNET -> {
                emit(Result.Error(ErrorType.NO_INTERNET))
            }

            CONTENT -> {
                emit(
                    Result.Success(
                        data = vacancyMapper.mapDtoToModel(response as VacancySearchResponse)
                    )
                )
            }

            else -> {
                emit(Result.Error(ErrorType.SERVER_THROWABLE))
            }
        }
    }

    override fun getSimilarVacancies(vacancyId: String): Flow<Result<VacancyListData>> = flow {
        val response = networkClient.doRequest(
            SimilarVacancyRequest(vacancyId)
        )
        when (response.resultCode) {
            NO_INTERNET -> {
                emit(Result.Error(ErrorType.NO_INTERNET))
            }

            CONTENT -> {
                emit(
                    Result.Success(
                        data = vacancyMapper.mapDtoToModel(response as VacancySearchResponse)
                    )
                )
            }

            else -> {
                emit(Result.Error(ErrorType.SERVER_THROWABLE))
            }
        }
    }

    override fun getFilterParameters(): FilterParameters {
        return filterMapper.mapDtoToFilterParameters(filterStorage.getFilterParameters())
    }

    private fun prepareSearchQueryMap(text: String, filter: FilterParameters): Map<String, String> {
        val map: HashMap<String, String> = HashMap()
        map["text"] = text

//        if (filter.area != null) {
//            map["area"] = filter.area.id
//        }
//        if (filter.industry != null) {
//            map["industry"] = filter.industry.id
//        }
//        if (filter.salary != null) {
//            map["salary"] = filter.salary.toString()
//        }
//        if (filter.onlyWithSalary != null) {
//            map["only_with_salary"] = filter.onlyWithSalary.toString()
//        }

        return map
    }
}
