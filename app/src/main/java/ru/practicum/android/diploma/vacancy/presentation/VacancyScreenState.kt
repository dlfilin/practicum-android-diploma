package ru.practicum.android.diploma.vacancy.presentation

import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

sealed interface VacancyScreenState {
    object Loading : VacancyScreenState
    object Error : VacancyScreenState
    data class Content(val vacancy: Vacancy) : VacancyScreenState
}
