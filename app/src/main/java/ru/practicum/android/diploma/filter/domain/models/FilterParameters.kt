package ru.practicum.android.diploma.filter.domain.models

data class FilterParameters(
    val area: Area? = null,
    val country: Country? = null,
    val industry: Industry? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false
) {
    val isNotEmpty: Boolean get() = area != null || industry != null || salary != null || onlyWithSalary
}
