package ru.practicum.android.diploma.search.domain.model

class QuerySearch(
    var page: Int,
    var text: String,
    val perPage: Int = 20
)
