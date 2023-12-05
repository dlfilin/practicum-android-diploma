package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.common.data.network.dto.Response

data class IndustryResponse(
    val industry: List<IndustryDto>
) : Response()
