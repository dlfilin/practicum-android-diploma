package ru.practicum.android.diploma.common.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
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
import ru.practicum.android.diploma.common.data.storage.FilterStorage
import ru.practicum.android.diploma.common.data.storage.SharedPrefsFilterStorage
import ru.practicum.android.diploma.common.data.storage.SharedPrefsFilterStorage.Companion.SEARCH_FILTER_PREFERENCES
import ru.practicum.android.diploma.common.mappers.FilterMapper
import ru.practicum.android.diploma.search.data.mapper.VacancyResponseMapper

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "dataBase.db")
            .fallbackToDestructiveMigration()
            .build()
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

    factory {
        FilterMapper()
    }

    single {
        androidContext().getSharedPreferences(
            SEARCH_FILTER_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    factory {
        Gson()
    }

    single<FilterStorage> {
        SharedPrefsFilterStorage(
            sharedPrefs = get(),
            gson = get()
        )
    }
}
