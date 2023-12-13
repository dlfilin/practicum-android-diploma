package ru.practicum.android.diploma.search.domain.model

data class SearchQuery(
    var text: String,
    var page: Int,
    val perPage: Int
)
