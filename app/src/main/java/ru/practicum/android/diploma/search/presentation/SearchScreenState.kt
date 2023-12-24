package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.search.domain.model.VacancyListData

sealed interface SearchScreenState {
    data class Content(
        val vacancyData: VacancyListData,
        val isLoading: Boolean
    ) : SearchScreenState

    data object Default : SearchScreenState
    data object InitialLoading : SearchScreenState
    data class Error(val error: ErrorType) : SearchScreenState
    data object Empty : SearchScreenState
}
