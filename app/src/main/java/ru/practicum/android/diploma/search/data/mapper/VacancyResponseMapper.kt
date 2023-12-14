package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.search.data.dto.VacancyDto
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.search.domain.model.VacancyItem
import ru.practicum.android.diploma.search.domain.model.VacancyListData

class VacancyResponseMapper {
    fun mapDtoToModel(vacancyDto: VacancySearchResponse) = VacancyListData(
        found = vacancyDto.found,
        items = mapDtoToVacancyList(vacancyDto.items),
        page = vacancyDto.page,
        pages = vacancyDto.pages,
        perPage = vacancyDto.perPage
    )

    private fun mapDtoToVacancyList(listDto: List<VacancyDto>): List<VacancyItem> {
        val vacancyList = mutableListOf<VacancyItem>()
        for (vacancyDto in listDto) {
            val vacancy = VacancyItem(
                id = vacancyDto.id,
                vacancyName = vacancyDto.name,
                area = vacancyDto.area.name,
                salaryFrom = vacancyDto.salary?.from,
                salaryTo = vacancyDto.salary?.to,
                salaryCurrency = vacancyDto.salary?.currency,
                employerName = vacancyDto.employer.name,
                employerLogoUrl = vacancyDto.employer.logoUrls?.logoUrl
            )
            vacancyList.add(vacancy)
        }
        return vacancyList
    }
}
