package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.common.data.network.dto.Response

data class VacancySearchResponse(
    val found: Int,
    val items: List<VacancyDto>,
    val page: Int,
    val pages: Int,
    @SerializedName("per_page")
    val perPage: Int
) : Response()
