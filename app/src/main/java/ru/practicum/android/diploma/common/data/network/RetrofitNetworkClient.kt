package ru.practicum.android.diploma.common.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApiService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response().apply { resultCode = -1 }

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is VacancySearchRequest -> hhApiService.searchVacancy(dto.expression)
                    else -> Response().apply { resultCode = 400 }
                }
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
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
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    companion object {
        const val HH_BASE_URL = "https://api.hh.ru/"
    }
}
