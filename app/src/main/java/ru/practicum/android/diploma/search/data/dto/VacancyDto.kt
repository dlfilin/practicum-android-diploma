package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.search.domain.model.VacancyItem

data class VacancyDto(
    val id: String,
    val name: String,
    val area: VacancyAreaDto,
    val salary: SalaryDto?,
    val employer: EmployerDto
) {
    fun toVacancyItem(): VacancyItem = VacancyItem(
        id = this.id,
        vacancyName = this.name,
        area = this.area.name,
        salaryFrom = this.salary?.from,
        salaryTo = this.salary?.to,
        salaryCurrency = this.salary?.currency,
        employerName = this.employer.name,
        employerLogoUrl = this.employer.logoUrls?.logoUrl
    )
}
