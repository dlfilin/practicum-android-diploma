package ru.practicum.android.diploma

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.common.di.dataModule
import ru.practicum.android.diploma.common.di.domainModule
import ru.practicum.android.diploma.common.di.repositoryModule
import ru.practicum.android.diploma.common.di.uiModule
import ru.practicum.android.diploma.common.di.viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                repositoryModule,
                domainModule,
                viewModelModule,
                uiModule
            )
        }
    }

}
