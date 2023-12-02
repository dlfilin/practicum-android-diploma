package ru.practicum.android.diploma.filter.data.dto

data class AreaDto(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: List<AreaDto>?,
    val currentPage: Int,
    val maxPages: Int
)