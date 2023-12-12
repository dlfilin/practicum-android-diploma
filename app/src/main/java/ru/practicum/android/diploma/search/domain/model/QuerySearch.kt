package ru.practicum.android.diploma.search.domain.model

data class QuerySearch(
    var page:Int,
    var text:String,
    var perPage: Int = 20
)
