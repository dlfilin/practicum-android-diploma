package ru.practicum.android.diploma.filter.domain.models

import ru.practicum.android.diploma.filter.domain.impl.Industry

data class FilterParameters(
    val area: Area? = null,
    val industry: Industry? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false
) {
    val isNotEmpty: Boolean get() = (area != null || industry != null || salary != null || onlyWithSalary)
}
