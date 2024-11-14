package com.example.ontime.di

import android.content.Context
import android.content.SharedPreferences
import com.example.ontime.routine.data.RunningRoutineRepositoryImpl
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.domain.usecase.GetTasksUseCase
import com.example.ontime.routine.domain.usecase.SaveTasksUseCase
import com.example.ontime.routine.presentation.RunningRoutineViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("ontime_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRunningRoutineRepository(sharedPreferences: SharedPreferences): RunningRoutineRepository {
        return RunningRoutineRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideGetTasksUseCase(repository: RunningRoutineRepository): GetTasksUseCase {
        return GetTasksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveTasksUseCase(repository: RunningRoutineRepository): SaveTasksUseCase {
        return SaveTasksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideRunningRoutineViewModelFactory(
        getTasksUseCase: GetTasksUseCase,
        saveTasksUseCase: SaveTasksUseCase
    ): RunningRoutineViewModelFactory {
        return RunningRoutineViewModelFactory(getTasksUseCase, saveTasksUseCase)
    }
}
