package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.common.data.network.dto.Response

data class VacancySearchResponse(
    val found: Int,
    val items: List<VacancyDto>,
    val page: Int,
    val pages: Int,
    val perPage : Int
) : Response()
