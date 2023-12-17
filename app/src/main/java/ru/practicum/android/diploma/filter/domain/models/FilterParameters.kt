package ru.practicum.android.diploma.filter.domain.models

data class FilterParameters(
    val area: Area? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false
) {
    val isNotEmpty: Boolean get() = area != null || salary != null || onlyWithSalary
}
