package ru.practicum.android.diploma.vacancy.presentation

import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

data class VacancyScreenState(
    val vacancy: Vacancy = Vacancy()
)
