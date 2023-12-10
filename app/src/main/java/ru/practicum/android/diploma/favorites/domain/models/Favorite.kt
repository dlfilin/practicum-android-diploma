package ru.practicum.android.diploma.favorites.domain.models

data class Favorite(
    val id: String,
    val nameVacancies: String,
    val logoUrl: String? = null,
    val salary: String? = null,
    val nameCompany: String,
    val city: String? = null,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val description: String? = null,
    val keySkills: String? = null,
    val contactPerson: String? = null,
    val contactEmail: String? = null,
    val contactPhone: String? = null,
    val comment: String? = null,
    val alternateUrl: String,
    val addTime: Long
)
