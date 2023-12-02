package ru.practicum.android.diploma.common.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.filter.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryResponse
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailResponse

interface HhApiService {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Мои hh вакансии (danila.filin@gmail.com)"
    )
    @GET("/vacancies")
    suspend fun searchVacancy(@Path("expression") expression: String): VacancySearchResponse

    // Запрос на поиск вакансий когда будут фильтры
//    @Headers(
//        "Authorization: Bearer YOUR_TOKEN",
//        "HH-User-Agent: Application Name (name@example.com)"
//    )
//    @GET("/vacancies")
//    suspend fun searchVacancy(@QueryMap options: Map<String, String>): VacancySearchResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Мои hh вакансии (danila.filin@gmail.com)"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetail(@Path("vacancy_id") id: String): VacancyDetailResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Мои hh вакансии (danila.filin@gmail.com)"
    )
    @GET("/areas")
    suspend fun getAllArea(): AreaResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Мои hh вакансии (danila.filin@gmail.com)"
    )
    @GET("/industries")
    suspend fun getAllIndustry(): IndustryResponse
}
