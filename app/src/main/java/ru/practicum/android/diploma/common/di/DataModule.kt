package ru.practicum.android.diploma.common.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.common.data.db.AppDataBase
import ru.practicum.android.diploma.common.data.network.HhApiService
import ru.practicum.android.diploma.common.data.network.NetworkClient
import ru.practicum.android.diploma.common.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.search.data.mapper.VacancyResponseMapper

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "dataBase.db").build()
    }

    single<HhApiService> {
        Retrofit.Builder()
            .baseUrl(RetrofitNetworkClient.HH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HhApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            context = androidContext(),
            hhApiService = get()
        )
    }

    factory {
        VacancyResponseMapper()
    }
}
