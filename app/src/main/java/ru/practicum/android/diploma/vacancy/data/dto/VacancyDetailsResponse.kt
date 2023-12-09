package ru.practicum.android.diploma.vacancy.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.search.data.dto.AddressDto
import ru.practicum.android.diploma.search.data.dto.EmployerDto
import ru.practicum.android.diploma.search.data.dto.SalaryDto
import ru.practicum.android.diploma.search.data.dto.VacancyAreaDto

data class VacancyDetailsResponse(
    val id: String,
    val name: String,
    val salary: SalaryDto?,
    val employer: EmployerDto,
    val area: VacancyAreaDto,
    val experience: ExperienceDto?,
    val employment: EmploymentDto?,
    val schedule: ScheduleDto?,
    val description: String,
    @SerializedName("key_skills")
    val keySkills: List<KeySkillDto>?,
    val contacts: ContactsDto?,
    val address: AddressDto?,
    @SerializedName("alternate_url")
    val alternateUrl: String
) : Response()
