package ru.practicum.android.diploma.search.domain.model

data class VacancyItem(
    val id: String,
    val vacancyName: String,
    val area: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,
    val employerName: String,
    val employerLogoUrl: String?
)
