package ru.practicum.android.diploma.filter.presentation

import ru.practicum.android.diploma.filter.domain.models.FilterParameters

data class FilterScreenState(
    val isApplyBtnVisible: Boolean,
    val editableFilter: FilterParameters,
) {
    val isClearBtnVisible: Boolean
        get() = editableFilter.isNotEmpty
}
