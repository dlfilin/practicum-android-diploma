package ru.practicum.android.diploma.common.data.storage.dto

import ru.practicum.android.diploma.filter.domain.impl.Industry
import ru.practicum.android.diploma.filter.domain.models.Area

data class FilterParametersDto(
    val area: Area?,
    val industry: Industry?,
    val salary: Int?,
    val onlyWithSalary: Boolean
)
