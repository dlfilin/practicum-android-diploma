package ru.practicum.android.diploma.common.data.network.dto

sealed interface Request {
    data class VacancySearchRequest(
        val options: Map<String, String>
    ) : Request

    data class VacancyDetailRequest(
        val vacancyId: String
    ) : Request

    data class SimilarVacancyRequest(
        val vacancyId: String,
        val options: Map<String, String>
    ) : Request

    data object AreaRequest : Request
    data class AreasByIdRequest(
        val areaId: String
    ) : Request

    data object CountryRequest : Request
    data object IndustryRequest : Request
}
