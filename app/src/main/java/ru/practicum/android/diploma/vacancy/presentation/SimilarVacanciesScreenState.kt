package ru.practicum.android.diploma.vacancy.presentation

import ru.practicum.android.diploma.search.domain.model.VacancyListData

sealed interface SimilarVacanciesScreenState {
    data class Content(
        val vacancyData: VacancyListData
    ) : SimilarVacanciesScreenState

    data object Loading : SimilarVacanciesScreenState
    data object Error : SimilarVacanciesScreenState
    data object InternetThrowable : SimilarVacanciesScreenState
    data object Empty : SimilarVacanciesScreenState
}
