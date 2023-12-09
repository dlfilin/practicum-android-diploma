package ru.practicum.android.diploma.vacancy.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.CONTENT
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient.Companion.NO_INTERNET
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.data.mapper.VacancyMapper
import ru.practicum.android.diploma.vacancy.domain.api.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyMapper: VacancyMapper
) : VacancyRepository {
    override fun getVacancy(vacancyId: String): Flow<Result<Vacancy>> = flow {
        val response = networkClient.doRequest(VacancyDetailRequest(vacancyId))
        Log.d("DEBUG", response.toString())

        when (response.resultCode) {
            NO_INTERNET -> {
                emit(Result.Error(ErrorType.NO_INTERNET))
            }

            CONTENT -> {
                emit(
                    Result.Success(
                        data = vacancyMapper.map(response as VacancyDetailsResponse)
                    )
                )
            }

            else -> {
                emit(Result.Error(ErrorType.SERVER_THROWABLE))
            }
        }
    }
}
