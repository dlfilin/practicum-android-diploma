package ru.practicum.android.diploma.filter.presentation

import ru.practicum.android.diploma.filter.domain.models.FilterParameters

data class FilterScreenState(
    private val entryFilter: FilterParameters,
    val currentFilter: FilterParameters,
) {
    val isApplyBtnVisible: Boolean
        get() = entryFilter != currentFilter
    val isClearBtnVisible: Boolean
        get() = currentFilter.isNotEmpty
}
