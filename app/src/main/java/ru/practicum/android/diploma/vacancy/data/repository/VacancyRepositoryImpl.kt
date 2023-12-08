package ru.practicum.android.diploma.vacancy.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.network.dto.ErrorType
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.CONTENT
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.NO_INTERNET
import ru.practicum.android.diploma.common.data.network.dto.Resource
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.data.mapper.VacancyMapper
import ru.practicum.android.diploma.vacancy.domain.api.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy


class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyMapper: VacancyMapper
) : VacancyRepository {
    override fun getVacancy(vacancyId: String): Flow<Resource<Vacancy>> = flow {
        val response = networkClient.doRequest(VacancyDetailRequest(vacancyId))
        Log.d("DEBUG", response.toString())

        when (response.resultCode) {
            NO_INTERNET -> {
                emit(Resource.Error(ErrorType.NO_INTERNET))
            }

            CONTENT -> {
                emit(
                    Resource.Success(
                        data = vacancyMapper.map(response as VacancyDetailsResponse)
                    )
                )
            }

            else -> {
                emit(Resource.Error(ErrorType.SERVER_ERROR))
            }
        }
    }
}
