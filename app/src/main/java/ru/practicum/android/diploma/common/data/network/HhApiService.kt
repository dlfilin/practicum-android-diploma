package ru.practicum.android.diploma.common.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.filter.data.dto.AreaListDto
import ru.practicum.android.diploma.filter.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.data.dto.CountryDto
import ru.practicum.android.diploma.filter.data.dto.IndustryListDto
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse

interface HhApiService {
    @Headers(TOKEN_BEARER_STRING, USER_AGENT_STRING)
    @GET("/vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): VacancySearchResponse

    @Headers(TOKEN_BEARER_STRING, USER_AGENT_STRING)
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(@Path("vacancy_id") id: String): VacancyDetailsResponse

    @Headers(TOKEN_BEARER_STRING, USER_AGENT_STRING)
    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun searchSimilarVacancies(@Path("vacancy_id") id: String): VacancySearchResponse

    @Headers(TOKEN_BEARER_STRING, USER_AGENT_STRING)
    @GET("/areas")
    suspend fun getCountries(): List<CountryDto>

    @Headers(TOKEN_BEARER_STRING, USER_AGENT_STRING)
    @GET("/areas")
    suspend fun getAllAreas(): List<AreaListDto>

    @Headers(TOKEN_BEARER_STRING, USER_AGENT_STRING)
    @GET("/areas/{area_id}")
    suspend fun getAreasForId(@Path("area_id") id: String): AreaResponse

    @Headers(TOKEN_BEARER_STRING, USER_AGENT_STRING)
    @GET("/industries")
    suspend fun getAllIndustries(): List<IndustryListDto>

    companion object {
        private const val USER_AGENT_STRING = "HH-User-Agent: my_hh_vacancies (danila.filin@gmail.com)"
        private const val TOKEN_BEARER_STRING = "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}"
    }

}

