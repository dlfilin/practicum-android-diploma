package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.network.dto.ErrorType
import ru.practicum.android.diploma.common.data.network.dto.Resource
import ru.practicum.android.diploma.vacancy.domain.api.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.api.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

class VacancyInteractorImpl(private val vacancyRepository: VacancyRepository) : VacancyInteractor {
    override fun getVacancy(vacancyId: String): Flow<Pair<Vacancy?, ErrorType?>> {
        return vacancyRepository.getVacancy(vacancyId).map { vacancyResponse ->
            when(vacancyResponse) {
                is Resource.Error -> {
                    Pair(null, vacancyResponse.errorType)
                }
                is Resource.Success -> {
                    Pair(vacancyResponse.data, null)
                }
            }
        }
    }
}
