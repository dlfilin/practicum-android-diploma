package ru.practicum.android.diploma.filter.domain.models

import ru.practicum.android.diploma.filter.domain.impl.Industry

data class FilterParameters(
    val area: Area? = null,
    val industry: Industry? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false
)
