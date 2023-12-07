package ru.practicum.android.diploma.common.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.filter.data.dto.AreaRequest
import ru.practicum.android.diploma.filter.data.dto.IndustryRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.vacancy.data.dto.SimilarVacancyRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailRequest
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApiService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response().apply { resultCode = NO_INTERNET }

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is VacancySearchRequest -> hhApiService.searchVacancies(dto.text)
                    is VacancyDetailRequest -> hhApiService.getVacancyDetails(dto.vacancyId)
                    is SimilarVacancyRequest -> hhApiService.searchSimilarVacancies(dto.vacancyId)
                    is AreaRequest -> hhApiService.getAllAreas()
                    is IndustryRequest -> hhApiService.getAllIndustries()
                    else -> Response().apply { resultCode = BAD_REQUEST }
                }
                response.apply { resultCode = CONTENT }
            } catch (e: IOException) {
                Response().apply {
                    resultCode = SERVER_THROWABLE
                    message = e.message
                }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return false
    }

    companion object {
        const val HH_BASE_URL = "https://api.hh.ru/"
        const val NO_INTERNET = -1
        const val CONTENT = 200
        const val BAD_REQUEST = 400
        const val SERVER_THROWABLE = 500
    }
}
