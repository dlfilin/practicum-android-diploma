package ru.practicum.android.diploma.filter.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.db.AppDataBase
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.storage.FilterStorage
import ru.practicum.android.diploma.common.mappers.FilterMapper
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.data.dto.AreaRequest
import ru.practicum.android.diploma.filter.data.dto.AreaResponse
import ru.practicum.android.diploma.filter.data.dto.CountryRequest
import ru.practicum.android.diploma.filter.data.dto.CountryResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryRequest
import ru.practicum.android.diploma.filter.data.dto.IndustryResponse
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.Area
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.FilterParameters
import ru.practicum.android.diploma.filter.domain.models.Industry

class FilterRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDataBase,
    private val filterStorage: FilterStorage,
    private val filterMapper: FilterMapper
) : FilterRepository {

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

    override fun checkApplyBtnVisible(): Boolean {
        val mainFilterParam = filterStorage.getMainFilterParameters()
        val editableFilterParam = filterStorage.getEditableFilterParameters()
        return mainFilterParam != editableFilterParam
    }

    override fun getCountries(): Flow<List<Country>> = database.filterDao().getCountries()
        .map { list -> list.map { filterMapper.mapEntityToDomainModel(it) }.sortedBy { it.id } }

    override fun getIndustries(): Flow<List<Industry>> = database.filterDao().getIndustries()
        .map { list -> list.map { filterMapper.mapEntityToDomainModel(it) }.sortedBy { it.name } }

    override fun getEditableFilter(): FilterParameters {
        return filterMapper.mapToDomainModel(filterStorage.getEditableFilterParameters())
    }

    override fun updateEditableFilter(filter: FilterParameters) {
        filterStorage.saveEditableFilterParameters(filterMapper.mapToDto(filter))
    }

    override fun saveIndustry(industry: Industry) {
        val editableFilterParam = filterStorage.getEditableFilterParameters()
        val newEditableFilterParam = editableFilterParam.copy(
            industry = filterMapper.mapIndustryToDto(
                industry
            )
        )
        filterStorage.saveEditableFilterParameters(
            newEditableFilterParam
        )
    }

    override fun saveCountry(country: Country) {
        val editableFilterParam = filterStorage.getEditableFilterParameters()
        val newEditableFilterParam = editableFilterParam.copy(
            country = filterMapper.mapCountryToDto(
                country
            )
        )
        filterStorage.saveEditableFilterParameters(
            newEditableFilterParam
        )
    }

    override fun saveArea(area: Area) {
        val editableFilterParam = filterStorage.getEditableFilterParameters()
        val newEditableFilterParam = editableFilterParam.copy(
            area = filterMapper.mapAreaToDto(
                area
            )
        )
        filterStorage.saveEditableFilterParameters(
            newEditableFilterParam
        )
    }

    override fun saveEditableInMainFilter() {
        val editableFilterParam = filterStorage.getEditableFilterParameters()
        filterStorage.saveMainFilterParameters(editableFilterParam)
    }

    override fun saveMainInEditableFilter() {
        val mainFilterParam = filterStorage.getMainFilterParameters()
        filterStorage.saveEditableFilterParameters(mainFilterParam)
    }
}



