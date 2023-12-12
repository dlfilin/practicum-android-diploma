package ru.practicum.android.diploma.search.domain.model

data class VacancyListData(
    val found: Int,
    val items: List<VacancyItem>,
    val page: Int,
    val pages: Int,
    val perPage: Int
)
