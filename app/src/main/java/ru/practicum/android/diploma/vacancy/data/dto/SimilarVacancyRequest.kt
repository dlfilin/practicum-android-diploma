package ru.practicum.android.diploma.vacancy.data.dto

data class SimilarVacancyRequest(
    val vacancyId: String,
    val options: Map<String, String>
)
