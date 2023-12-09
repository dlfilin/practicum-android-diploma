package ru.practicum.android.diploma.common.di

import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
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
        val interceptop = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptop)
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.HH_API_BASE_URL)
            .client(client)
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
