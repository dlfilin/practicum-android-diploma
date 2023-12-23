package ru.practicum.android.diploma.vacancy.presentation

import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

sealed interface VacancyScreenState {
    data object Loading : VacancyScreenState
    data object Error : VacancyScreenState
    data object InternetThrowable : VacancyScreenState
    data class Content(val vacancy: Vacancy) : VacancyScreenState
}
