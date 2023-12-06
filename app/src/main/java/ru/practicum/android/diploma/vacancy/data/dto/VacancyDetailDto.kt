package ru.practicum.android.diploma.vacancy.data.dto

import ru.practicum.android.diploma.search.data.dto.EmployerDto
import ru.practicum.android.diploma.search.data.dto.SalaryDto
import ru.practicum.android.diploma.search.data.dto.VacancyAreaDto

data class VacancyDetailDto(
    val id: String,
    val name: String,
    val salary: SalaryDto?,
    val employer: EmployerDto?,
    val area: VacancyAreaDto,
    val experience: ExperienceDto?,
    val employment: EmploymentDto?,
    val schedule: ScheduleDto?,
    val description: String,
    val keySkills: List<SkillsDto>,
    val contacts: ContactsDto?
)
