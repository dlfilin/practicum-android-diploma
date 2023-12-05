package ru.practicum.android.diploma.vacancy.data.dto

import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancyDto

data class VacancyDetailResponse(
    val vacancy: VacancyDto
) : Response()
