package ru.practicum.android.diploma.common.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailResponse
import ru.practicum.android.diploma.search.data.dto.VacancySearchResponse

interface HhApiService {
    @Headers(
        "Authorization: Bearer YOUR_TOKEN",
        "HH-User-Agent: Мои hh вакансии (danila.filin@gmail.com)"
    )
    @GET("/vacancies")
    suspend fun searchVacancy(@Path("expression") expression: String): VacancySearchResponse

    // Запрос на поиск вакансий когда будут фильтры
//    @Headers(
//        "Authorization: Bearer YOUR_TOKEN",
//        "HH-User-Agent: Application Name (name@example.com)"
//    )
//    @GET("/vacancies/{vacancy_id}")
//    suspend fun searchVacancy(@QueryMap options: Map<String, String>): VacancySearchResponse

    @Headers("Authorization: Bearer YOUR_TOKEN",
        "HH-User-Agent: Мои hh вакансии (danila.filin@gmail.com)")
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyDetail(@Path("vacancy_id") id: String): VacancyDetailResponse
}
