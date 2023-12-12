package ru.practicum.android.diploma.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.data.db.entity.IndustryEntity

@Dao
interface FilterDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addCountry(country: CountryEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//     suspend fun addArea(area: AreaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addIndustry(industry: IndustryEntity)

    @Query("SELECT * FROM industry")
    fun getIndustries(): Flow<List<IndustryEntity>>


}
