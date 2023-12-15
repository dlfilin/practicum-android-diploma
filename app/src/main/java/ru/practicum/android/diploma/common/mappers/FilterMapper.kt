package ru.practicum.android.diploma.common.mappers

import ru.practicum.android.diploma.common.data.storage.dto.FilterParametersDto
import ru.practicum.android.diploma.filter.data.db.entity.AreaEntity
import ru.practicum.android.diploma.filter.data.db.entity.CountryEntity
import ru.practicum.android.diploma.filter.data.db.entity.IndustryEntity
import ru.practicum.android.diploma.filter.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.data.dto.CountryResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryResponse
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.domain.models.Industry

class FilterMapper {

    fun mapDtoToFilterParameters(dto: FilterParametersDto) = FilterParameters(
        area = dto.area,
        industry = dto.industry,
        salary = dto.salary,
        onlyWithSalary = dto.onlyWithSalary
    )

    fun mapEntityToDomainModel(countryItem: AreaEntity): Area {
        return Area(
            id = countryItem.id,
            name = countryItem.name,
        )
    }

    fun mapEntityToDomainModel(countryItem: CountryEntity): Country {
        return Country(
            id = countryItem.id,
            name = countryItem.name,
        )
    }

    fun mapEntityToDomainModel(industryItem: IndustryEntity): Industry {
        return Industry(
            id = industryItem.id,
            name = industryItem.name,
            isChecked = false
        )
    }

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
}
