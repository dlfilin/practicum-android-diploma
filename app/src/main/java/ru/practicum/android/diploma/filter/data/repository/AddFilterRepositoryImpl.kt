package ru.practicum.android.diploma.filter.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.common.data.db.AppDataBase
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.util.NetworkResult
import ru.practicum.android.diploma.filter.data.db.entity.IndustryEntity
import ru.practicum.android.diploma.filter.data.dto.IndustryRequest
import ru.practicum.android.diploma.filter.data.dto.IndustryResponse
import ru.practicum.android.diploma.filter.domain.api.AddFilterRepository
import ru.practicum.android.diploma.filter.domain.models.Industry

class AddFilterRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDataBase,
) : AddFilterRepository {

    override suspend fun getIndustry() {
        when (val result = networkClient.doRequest(IndustryRequest())) {
            is NetworkResult.Success -> {
                val data = mapIndustry(result.data as IndustryResponse)
                for (item in data) {
                    database.filterDao().addIndustry(item)
                }
            }

            is NetworkResult.Error -> {
                Log.e("Error Industry", "Ошибка при загрузке")
            }
        }
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

    private fun mapIndustry(industryDto: IndustryResponse): List<IndustryEntity> {
        val industryList = mutableListOf<IndustryEntity>()
        for (industryDtoItem in industryDto.industry) {
            val vacancy = IndustryEntity(
                id = industryDtoItem.id,
                name = industryDtoItem.name,
            )
            industryList.add(vacancy)
        }
        return industryList
    }
}



