package com.example.ontime.di

import com.example.ontime.routine.presentation.RunningRoutineViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(viewModel: RunningRoutineViewModel)

    companion object {
        lateinit var instance: AppComponent
    }
}