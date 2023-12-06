package ru.practicum.android.diploma.search.domain.model

import ru.practicum.android.diploma.search.data.dto.VacancyDto

data class VacancyListData(
    val found: Int,
    val items: List<VacancyDto>,
    val page: Int,
    val pages: Int,
    val perPage: Int


)
