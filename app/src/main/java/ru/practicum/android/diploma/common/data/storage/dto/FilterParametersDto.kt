package ru.practicum.android.diploma.common.data.storage.dto

import ru.practicum.android.diploma.filter.data.dto.AreaDto
import ru.practicum.android.diploma.filter.data.dto.CountryDto
import ru.practicum.android.diploma.filter.data.dto.IndustryDto

data class FilterParametersDto(
    val area: AreaDto?,
    val country: CountryDto?,
    val industry: IndustryDto?,
    val salary: Int?,
    val onlyWithSalary: Boolean
) {
    val isNotEmpty: Boolean
        get() = area != null || country != null || industry != null || salary != null || onlyWithSalary
}
