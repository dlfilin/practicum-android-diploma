package ru.practicum.android.diploma.filter.domain.impl

data class Industry(
    val id: String,
    val name: String,
    val industries: List<Industry>?,
    val currentPage: Int,
    val maxPages: Int
)
