package com.sabidos

import android.app.Application
import com.sabidos.infrastructure.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SabidosApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SabidosApplication)
            modules(appModules)
        }
    }

}