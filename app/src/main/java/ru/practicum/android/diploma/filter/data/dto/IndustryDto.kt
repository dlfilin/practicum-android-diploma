package ru.practicum.android.diploma.filter.data.dto

data class IndustryDto(
    val id: String,
    val name: String,
    val industries: List<IndustryDto>?,
    val currentPage: Int,
    val maxPages: Int
)
