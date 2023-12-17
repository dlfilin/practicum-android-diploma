package ru.practicum.android.diploma.vacancy.domain.models

data class Vacancy(
    val id: String,
    val vacancyName: String,
    val area: String,
    val experience: String?,
    val employment: String?,
    val schedule: String?,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,
    val employerName: String,
    val employerLogoUrl: String?,
    val address: String?,
    val description: String,
    val keySkills: List<String>?,
    val contacts: Contacts?,
    val alternateUrl: String
)
