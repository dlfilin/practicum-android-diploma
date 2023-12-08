package ru.practicum.android.diploma.search.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.CONTENT
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.NO_INTERNET
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.data.mapper.VacancyResponseMapper
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SearchRepositoryImpl(
    private val networkClient: NetworkClient, private val converter: VacancyResponseMapper
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
                        data = converter.mapDtoToModel(response as VacancySearchResponse)
                    )
                )
            }

            else -> {
                emit(Result.Error(ErrorType.SERVER_THROWABLE))
            }
        }
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
