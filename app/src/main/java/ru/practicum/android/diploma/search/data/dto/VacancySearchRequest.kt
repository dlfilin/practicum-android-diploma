package ru.practicum.android.diploma.search.data.dto

data class VacancySearchRequest(
    // expression для поисковых запросов, пока не сделан фильтр, потом можно удалить
    val expression: String,
    val options: Map<String, String>
)
