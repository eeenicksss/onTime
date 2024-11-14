package com.example.ontime

import android.app.Application
import com.example.ontime.di.AppComponent
import com.example.ontime.di.AppModule
import com.example.ontime.di.DaggerAppComponent

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Передаем applicationContext в AppModule
        AppComponent.instance = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()
    }
}
