package ru.practicum.android.diploma.vacancy.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.dto.Request
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.vacancy.data.mapper.VacancyMapper
import ru.practicum.android.diploma.vacancy.domain.api.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val vacancyMapper: VacancyMapper
) : VacancyRepository {
    override fun getVacancy(
        vacancyId: String
    ): Flow<NetworkResult<Vacancy>> = flow {
        when (val result = networkClient.doRequest(Request.VacancyDetailRequest(vacancyId))) {
            is NetworkResult.Success -> {
                val data = vacancyMapper.map(result.data as VacancyDetailsResponse)
                emit(NetworkResult.Success(data))
            }

            is NetworkResult.Error -> {
                emit(NetworkResult.Error(result.errorType!!))
            }
        }
    }
}
