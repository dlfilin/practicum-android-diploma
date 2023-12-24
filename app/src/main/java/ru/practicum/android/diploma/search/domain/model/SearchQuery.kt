package ru.practicum.android.diploma.search.domain.model

class SearchQuery(
    var page: Int,
    var text: String? = null,
    val perPage: Int = 20
)
