package ru.practicum.android.diploma.vacancy.data.dto

import ru.practicum.android.diploma.common.data.network.dto.Response

data class VacancyDetailResponse(
    val vacancy: VacancyDetailDto
) : Response()
