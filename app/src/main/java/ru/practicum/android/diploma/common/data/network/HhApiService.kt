package ru.practicum.android.diploma.common.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.filter.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryResponse
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailsResponse

interface HhApiService {
    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: my_hh_vacancies (danila.filin@gmail.com)"
    )
    @GET("/vacancies")
    suspend fun searchVacancies(@Query("text") text: String): VacancySearchResponse

    // Запрос на поиск вакансий когда будут фильтры
//    @Headers(
//        "Authorization: Bearer YOUR_TOKEN",
//        "HH-User-Agent: Application Name (name@example.com)"
//    )
//    @GET("/vacancies")
//    suspend fun searchVacancy(@QueryMap options: Map<String, String>): VacancySearchResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Application Мои_hh_вакансии (danila.filin@gmail.com)"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetails(@Path("vacancy_id") id: String): VacancyDetailsResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Application Мои_hh_вакансии (danila.filin@gmail.com)"
    )
    @GET("/vacancies/{vacancy_id}/similar_vacancies")
    suspend fun searchSimilarVacancies(@Path("vacancy_id") id: String): VacancySearchResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Application Мои_hh_вакансии (danila.filin@gmail.com)"
    )
    @GET("/areas")
    suspend fun getAllAreas(): AreaResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.HH_ACCESS_TOKEN}",
        "HH-User-Agent: Application Мои_hh_вакансии (danila.filin@gmail.com)"
    )
    @GET("/industries")
    suspend fun getAllIndustries(): IndustryResponse
}
