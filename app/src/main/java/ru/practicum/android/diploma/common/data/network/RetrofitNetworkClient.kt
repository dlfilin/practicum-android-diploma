package ru.practicum.android.diploma.common.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.data.dto.AreaRequest
import ru.practicum.android.diploma.filter.data.dto.CountryRequest
import ru.practicum.android.diploma.filter.data.dto.IndustryRequest
import ru.practicum.android.diploma.search.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.vacancy.data.dto.SimilarVacancyRequest
import ru.practicum.android.diploma.vacancy.data.dto.VacancyDetailRequest
import java.io.IOException

class RetrofitNetworkClient(
    private val context: Context,
    private val hhApiService: HhApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): NetworkResult<Response> {
        if (!isConnected()) return NetworkResult.Error(ErrorType.NO_INTERNET)

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is VacancySearchRequest -> hhApiService.searchVacancies(dto.options)
                    is VacancyDetailRequest -> hhApiService.getVacancyDetails(dto.vacancyId)
                    is SimilarVacancyRequest -> hhApiService.searchSimilarVacancies(dto.vacancyId)
                    is AreaRequest -> hhApiService.getAllAreas()
                    is CountryRequest -> hhApiService.getCountries()
                    is IndustryRequest -> hhApiService.getAllIndustries()
                    else -> throw BadRequestException()
                }
                NetworkResult.Success(response)
            } catch (e: IOException) {
                NetworkResult.Error(ErrorType.SERVER_THROWABLE)
            } catch (e: HttpException) {
                NetworkResult.Error(ErrorType.NON_200_RESPONSE)
            } catch (e: BadRequestException) {
                NetworkResult.Error(ErrorType.BAD_REQUEST)
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
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

    class BadRequestException : Exception()
}
