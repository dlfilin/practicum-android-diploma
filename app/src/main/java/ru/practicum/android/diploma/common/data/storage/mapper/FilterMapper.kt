package ru.practicum.android.diploma.common.data.storage.mapper

import ru.practicum.android.diploma.common.data.storage.dto.FilterParametersDto
import ru.practicum.android.diploma.filter.domain.models.FilterParameters

class FilterMapper {
    fun mapDtoToFilterParameters(dto: FilterParametersDto) = FilterParameters(
        area = dto.area,
        industry = dto.industry,
        salary = dto.salary,
        onlyWithSalary = dto.onlyWithSalary
    )

    fun mapFilterParametersToDto(filterParameters: FilterParameters) = FilterParametersDto(
        area = filterParameters.area,
        industry = filterParameters.industry,
        salary = filterParameters.salary,
        onlyWithSalary = filterParameters.onlyWithSalary
    )
}
