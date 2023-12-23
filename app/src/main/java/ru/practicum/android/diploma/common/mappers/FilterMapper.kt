package ru.practicum.android.diploma.common.mappers

import ru.practicum.android.diploma.common.data.storage.dto.FilterParametersDto
import ru.practicum.android.diploma.filter.data.dto.AreaDto
import ru.practicum.android.diploma.filter.data.dto.AreaListDto
import ru.practicum.android.diploma.filter.data.dto.CountryDto
import ru.practicum.android.diploma.filter.data.dto.IndustryDto
import ru.practicum.android.diploma.filter.data.dto.IndustryListDto
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.domain.models.Industry

class FilterMapper {

    fun mapToDomain(dto: FilterParametersDto) = FilterParameters(
        area = mapDtoToArea(dto.area),
        country = mapDtoToCountry(dto.country),
        industry = mapDtoToIndustry(dto.industry),
        salary = dto.salary,
        onlyWithSalary = dto.onlyWithSalary
    )

    fun mapToDto(model: FilterParameters) = FilterParametersDto(
        area = mapAreaToDto(model.area),
        country = mapCountryToDto(model.country),
        industry = mapIndustryToDto(model.industry),
        salary = model.salary,
        onlyWithSalary = model.onlyWithSalary
    )

    fun mapToDomain(dto: AreaListDto) = Area(
        id = dto.id,
        name = dto.name,
        parentId = dto.parentId!!
    )

    fun mapToDomain(dto: IndustryDto) = Industry(
        id = dto.id,
        name = dto.name,
    )

    fun mapToDomain(dto: CountryDto) = Country(
        id = dto.id,
        name = dto.name
    )

    private fun mapDtoToArea(dto: AreaDto?): Area? {
        return if (dto != null) {
            Area(
                id = dto.id,
                name = dto.name,
                parentId = dto.parentId
            )
        } else {
            null
        }
    }

    private fun mapDtoToCountry(dto: CountryDto?): Country? {
        return if (dto != null) {
            Country(
                id = dto.id,
                name = dto.name
            )
        } else {
            null
        }
    }

    private fun mapDtoToIndustry(dto: IndustryDto?): Industry? {
        return if (dto != null) {
            Industry(
                id = dto.id,
                name = dto.name,
            )
        } else {
            null
        }
    }

    private fun mapAreaToDto(model: Area?): AreaDto? {
        return if (model != null) {
            AreaDto(
                id = model.id,
                name = model.name,
                parentId = model.parentId
            )
        } else {
            null
        }
    }

    private fun mapCountryToDto(model: Country?): CountryDto? {
        return if (model != null) {
            CountryDto(
                id = model.id,
                name = model.name
            )
        } else {
            null
        }
    }

    private fun mapIndustryToDto(model: Industry?): IndustryDto? {
        return if (model != null) {
            IndustryDto(
                id = model.id,
                name = model.name
            )
        } else {
            null
        }
    }

    fun flattenIndustries(list: List<IndustryListDto>): List<Industry> {
        val result = mutableListOf<Industry>()
        list.forEach { item ->
            result += Industry(item.id, item.name)
            result.addAll(item.industries.map { mapToDomain(it) })
        }
        return result.sortedBy { it.name }
    }

    tailrec fun flattenAreas(remaining: List<AreaListDto>, collected: List<AreaListDto>): List<AreaListDto> {
        if (remaining.isEmpty()) return collected
        val head = remaining.first()
        val tail = remaining.drop(1)
        return flattenAreas(head.areas.plus(tail), collected.plus(head))
    }
}
