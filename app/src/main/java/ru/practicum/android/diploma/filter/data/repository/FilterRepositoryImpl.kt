package ru.practicum.android.diploma.filter.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.db.AppDataBase
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.mappers.FilterMapper
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.data.dto.AreaRequest
import ru.practicum.android.diploma.filter.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.data.dto.CountryRequest
import ru.practicum.android.diploma.filter.data.dto.CountryResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryRequest
import ru.practicum.android.diploma.filter.data.dto.IndustryResponse
import ru.practicum.android.diploma.filter.domain.api.AddFilterRepository
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Industry

class FilterRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDataBase,
    private val filterMapper: FilterMapper
) : AddFilterRepository {

    override suspend fun getAreaAndSaveDb() {
        when (val result = networkClient.doRequest(AreaRequest())) {
            is NetworkResult.Success -> {
                val data = filterMapper.mapAreaToEntity(result.data as AreaResponse)
                data.forEach {
                    database.filterDao().addArea(it)
                }
            }

            is NetworkResult.Error -> {
                Log.e("Error Area", "Error loading Area")
            }
        }
    }

    override suspend fun getCountryAndSaveDb() {
        when (val result = networkClient.doRequest(CountryRequest())) {
            is NetworkResult.Success -> {
                val data = filterMapper.mapCountryToEntity(result.data as CountryResponse)
                for (item in data) {
                    database.filterDao().addCountry(item)
                }
            }

            is NetworkResult.Error -> {
                Log.e("Error Country", "Error loading Country")
            }
        }
    }

    override suspend fun getIndustryAndSaveDb() {
        when (val result = networkClient.doRequest(IndustryRequest())) {
            is NetworkResult.Success -> {
                val data = filterMapper.mapIndustryToEntity(result.data as IndustryResponse)
                for (item in data) {
                    database.filterDao().addIndustry(item)
                }
            }

            is NetworkResult.Error -> {
                Log.e("Error Industry", "Error loading Industry")
            }
        }
    }

    override fun getAreas(): Flow<List<Area>> = database.filterDao().getAreas()
        .map { list -> list.map { filterMapper.mapEntityToDomainModel(it) } }

    override fun getCountries(): Flow<List<Country>> = database.filterDao().getCountries()
        .map { list -> list.map { filterMapper.mapEntityToDomainModel(it) }.sortedBy { it.id } }

    override fun getIndustries(): Flow<List<Industry>> = database.filterDao().getIndustries()
        .map { list -> list.map { filterMapper.mapEntityToDomainModel(it) }.sortedBy { it.name } }

}



