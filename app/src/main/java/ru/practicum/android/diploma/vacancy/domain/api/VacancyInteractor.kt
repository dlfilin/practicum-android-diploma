package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.common.util.Result
import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

interface VacancyInteractor {
    fun getVacancy(vacancyId: String): Flow<Result<Vacancy>>
}
