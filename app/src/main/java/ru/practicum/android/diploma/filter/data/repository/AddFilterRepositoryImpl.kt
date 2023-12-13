package ru.practicum.android.diploma.filter.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.db.AppDataBase
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.data.db.entity.CountryEntity
import ru.practicum.android.diploma.filter.data.db.entity.IndustryEntity
import ru.practicum.android.diploma.filter.data.dto.CountryRequest
import ru.practicum.android.diploma.filter.data.dto.CountryResponse
import ru.practicum.android.diploma.filter.data.dto.IndustryRequest
import ru.practicum.android.diploma.filter.data.dto.IndustryResponse
import ru.practicum.android.diploma.filter.domain.api.AddFilterRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Industry

class AddFilterRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDataBase,
) : AddFilterRepository {

    override suspend fun getCountryAndSaveDb() {
        when (val result = networkClient.doRequest(CountryRequest())) {
            is NetworkResult.Success -> {
                val data = mapCountryToEntity(result.data as CountryResponse)
                for (item in data) {
                    database.filterDao().addCountry(item)
                }
            }

            is NetworkResult.Error -> {
                Log.e("Error Country", "Error loading")
            }
        }
    }

    override suspend fun getIndustryAndSaveDb() {
        when (val result = networkClient.doRequest(IndustryRequest())) {
            is NetworkResult.Success -> {
                val data = mapIndustryToEntity(result.data as IndustryResponse)
                for (item in data) {
                    database.filterDao().addIndustry(item)
                }
            }

            is NetworkResult.Error -> {
                Log.e("Error Industry", "Error loading")
            }
        }
    }

    override fun getCountries(): Flow<List<Country>> = database.filterDao().getCountries()
        .map { list -> list.map { mup(it) } }

    private fun mup(countryItem: CountryEntity): Country {
        return Country(
            id = countryItem.id,
            name = countryItem.name,
        )
    }


    override fun getIndustries(): Flow<List<Industry>> = database.filterDao().getIndustries()
        .map { list -> list.map { mup(it) } }

    private fun mup(industryItem: IndustryEntity): Industry {
        return Industry(
            id = industryItem.id,
            name = industryItem.name,
            isChecked = false
        )
    }

    private fun mapIndustryToEntity(industryDto: IndustryResponse): List<IndustryEntity> {
        val industryList = mutableListOf<IndustryEntity>()
        for (industryDtoItem in industryDto.industry) {
            val industry = IndustryEntity(
                id = industryDtoItem.id,
                name = industryDtoItem.name,
            )
            industryList.add(industry)
        }
        return industryList
    }

    private fun mapCountryToEntity(countryDto: CountryResponse): List<CountryEntity> {
        val countryList = mutableListOf<CountryEntity>()
        for (countryDtoItem in countryDto.areas) {
            val country = CountryEntity(
                id = countryDtoItem.id,
                name = countryDtoItem.name,
            )
            countryList.add(country)
        }
        return countryList
    }
}


