package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.search.domain.model.VacancyListData

sealed interface SearchScreenState {
    data class Content(
        val vacancyData: VacancyListData,
    ) : SearchScreenState

    data object Default : SearchScreenState
    data object InitialLoading : SearchScreenState
    data object Error : SearchScreenState
    data object InternetThrowable : SearchScreenState
    data object Empty : SearchScreenState
    data class NextPageLoading(val isLoading: Boolean) : SearchScreenState
}
