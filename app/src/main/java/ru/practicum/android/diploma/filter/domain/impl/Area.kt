package ru.practicum.android.diploma.filter.domain.impl

data class Area(
    val id: String,
    val parentId: String?,
    val name: String,
    val areas: List<Area>?,
    val currentPage: Int,
    val maxPages: Int
)
