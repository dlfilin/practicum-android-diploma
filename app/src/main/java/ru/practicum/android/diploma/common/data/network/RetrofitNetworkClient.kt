package ru.practicum.android.diploma.common.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.common.data.network.dto.Response
import ru.practicum.android.diploma.common.util.ErrorType
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.data.dto.AreaRequest
import ru.practicum.android.diploma.filter.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.data.dto.AreasByIdRequest
import ru.practicum.android.diploma.filter.data.dto.CountryRequest
import ru.practicum.android.diploma.filter.data.dto.CountryResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryRequest
import ru.practicum.android.diploma.filter.data.dto.IndustryResponse
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
                    is SimilarVacancyRequest -> hhApiService.searchSimilarVacancies(dto.vacancyId, dto.options)
                    is AreaRequest -> AreaResponse(hhApiService.getAllAreas())
                    is AreasByIdRequest -> hhApiService.getAreasForId(dto.areaId)
                    is CountryRequest -> CountryResponse(hhApiService.getCountries())
                    is IndustryRequest -> IndustryResponse(hhApiService.getAllIndustries())
                    else -> Response()
                }
                NetworkResult.Success(response)
            } catch (e1: IOException) {
                Log.e("TAG", e1.toString())
                NetworkResult.Error(ErrorType.SERVER_THROWABLE)
            } catch (e2: HttpException) {
                Log.e("TAG", e2.toString())
                NetworkResult.Error(ErrorType.NON_200_RESPONSE)
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
}
