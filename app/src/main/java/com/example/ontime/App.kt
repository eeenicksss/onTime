package com.example.ontime

import android.app.Application
import com.example.ontime.di.DaggerAppComponent

class App : Application() {

    val appComponent by lazy {
        DaggerAppComponent.create()
    }

    override fun onCreate() {
        super.onCreate()
        // Здесь можно выполнить другие настройки, если нужно
    }
}