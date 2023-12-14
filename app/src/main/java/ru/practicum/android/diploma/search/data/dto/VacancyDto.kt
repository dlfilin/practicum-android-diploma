package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.search.domain.model.VacancyItem

data class VacancyDto(
    val id: String,
    val name: String,
    val area: VacancyAreaDto,
    val salary: SalaryDto?,
    val employer: EmployerDto
) {
    fun mapDtoToModel(): VacancyItem {
        return VacancyItem(
            id = id,
            vacancyName = name,
            area = area.name,
            salaryFrom = salary?.from,
            salaryTo = salary?.to,
            salaryCurrency = salary?.currency,
            employerName = employer.name,
            employerLogoUrl = employer.logoUrls?.logoUrl
        )
    }
}
