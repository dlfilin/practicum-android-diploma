package ru.practicum.android.diploma.search.data.dto

data class VacancySearchRequest(
    // text для поисковых запросов, пока не сделан фильтр, потом можно удалить
    val text: String,
    //  val options: Map<String, String>
)
