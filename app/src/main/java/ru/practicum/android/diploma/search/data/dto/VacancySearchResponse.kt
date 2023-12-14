package ru.practicum.android.diploma.search.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.search.domain.model.VacancyListData

data class VacancySearchResponse(
    val found: Int,
    val items: List<VacancyDto>,
    val page: Int,
    val pages: Int,
    @SerializedName("per_page")
    val perPage: Int
) : Response() {
    fun mapDtoToModel(): VacancyListData {
        return VacancyListData(
            found = found,
            items = items.map {it.mapDtoToModel()},
            page = page,
            pages = pages,
            perPage = perPage
        )
    }
}
