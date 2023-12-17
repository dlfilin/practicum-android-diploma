package ru.practicum.android.diploma.common.mappers

import ru.practicum.android.diploma.common.data.storage.dto.FilterParametersDto
import ru.practicum.android.diploma.filter.data.db.entity.AreaEntity
import ru.practicum.android.diploma.filter.data.db.entity.CountryEntity
import ru.practicum.android.diploma.filter.data.db.entity.IndustryEntity
import ru.practicum.android.diploma.filter.data.dto.AreaDto
import ru.practicum.android.diploma.filter.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.data.dto.CountryDto
import ru.practicum.android.diploma.filter.data.dto.CountryResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryDto
import ru.practicum.android.diploma.filter.data.dto.IndustryListDto
import ru.practicum.android.diploma.filter.data.dto.IndustryResponse
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.domain.models.Industry

class FilterMapper {

    fun mapToDomainModel(dto: FilterParametersDto) = FilterParameters(
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

    fun mapToDomainModel(dto: IndustryDto) = Industry(
        id = dto.id,
        name = dto.name,
    )
    fun mapToDomainModel(dto: CountryDto) = Country(
        id = dto.id,
        name = dto.name
    )

    fun mapIndustryToEntity(industryDto: IndustryResponse): List<IndustryEntity> {
        val industryList = mutableListOf<IndustryEntity>()
        for (industryDtoItem in industryDto.industry) {
            val industry = IndustryEntity(
                id = industryDtoItem.id,
                name = industryDtoItem.name,
            )
            industryList.add(industry)
        }
        return industryList
    }

    fun mapCountryToEntity(countryDto: CountryResponse): List<CountryEntity> {
        val countryList = mutableListOf<CountryEntity>()
        for (countryDtoItem in countryDto.areas) {
            val country = CountryEntity(
                id = countryDtoItem.id,
                name = countryDtoItem.name,
            )
            countryList.add(country)
        }
        return countryList
    }

    fun mapAreaToEntity(areaDto: AreaResponse): List<AreaEntity> {
        val areaList = mutableListOf<AreaEntity>()
        areaDto.areas.forEach {
            for (item in it.areas) {
                val area = AreaEntity(
                    id = item.id,
                    parentId = item.parentId,
                    name = item.name
                )
                areaList.add(area)
            }
        }
        return areaList
    }

    fun mapResponseToDomain(areaDto: AreaResponse): List<Area> {
        val areaList = mutableListOf<Area>()
        areaDto.areas.forEach {
            for (item in it.areas) {
                val area = Area(
                    id = item.id,
                    parentId = item.parentId,
                    name = item.name
                )
                areaList.add(area)
            }
        }
        return areaList
    }

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

    fun mapDtoToCountry(dto: CountryDto?): Country? {
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
            result.addAll(item.industries.map { mapToDomainModel(it) })
        }
        return result.sortedBy { it.name }
    }
}
