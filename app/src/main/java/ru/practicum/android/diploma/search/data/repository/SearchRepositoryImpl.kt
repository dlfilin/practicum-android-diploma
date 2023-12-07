package ru.practicum.android.diploma.search.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.CONTENT
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.NO_INTERNET
import ru.practicum.android.diploma.common.data.network.dto.ErrorType
import ru.practicum.android.diploma.common.data.network.dto.Resource
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.data.mapper.VacancyResponseMapper
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val convertor: VacancyResponseMapper
) : SearchRepository {

    override fun searchVacancy(text: String): Flow<Resource<VacancyListData>> = flow {
        val response = networkClient.doRequest(VacancySearchRequest(text))
        when (response.resultCode) {
            NO_INTERNET -> {
                emit(Resource.Error(ErrorType.NO_INTERNET))
            }

            CONTENT -> {
                emit(
                    Resource.Success(
                        data = convertor.mapDtoToModel(response as VacancySearchResponse)
                    )
                )
            }

            else -> {
                emit(Resource.Error(ErrorType.SERVER_ERROR))
            }
        }
    }
}
