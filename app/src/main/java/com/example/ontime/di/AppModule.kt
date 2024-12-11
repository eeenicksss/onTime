package com.example.ontime.di

import android.content.Context
import android.content.SharedPreferences
import com.example.ontime.create.presentation.CreateRoutineViewModel
import com.example.ontime.list.data.RoutinesListRepositoryImpl
import com.example.ontime.list.domain.RoutinesListRepository
import com.example.ontime.routine.data.RunningRoutineRepositoryImpl
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.domain.usecase.GetTasksUseCase
import com.example.ontime.routine.domain.usecase.SaveTasksUseCase
import com.example.ontime.routine.presentation.RunningRoutineViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    fun provideRoutinesListRepository(sharedPreferences: SharedPreferences): RoutinesListRepository {
        return RoutinesListRepositoryImpl(sharedPreferences) // Используйте реальную реализацию
    }

    @Provides
    @Singleton
    fun provideGetTasksUseCase(repository: RunningRoutineRepository, routineId: String): GetTasksUseCase {
        return GetTasksUseCase(repository, routineId)
    }

    @Provides
    @Singleton
    fun provideSaveTasksUseCase(repository: RunningRoutineRepository): SaveTasksUseCase {
        return SaveTasksUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideRunningRoutineViewModelFactory(
        repository: RunningRoutineRepository,
        dispatcher: CoroutineDispatcher, // Передаем диспетчер
        routineId: String
    ): RunningRoutineViewModelFactory {
        return RunningRoutineViewModelFactory(repository, dispatcher, routineId)
    }

    @Provides
    @Singleton
    fun provideCreateRoutineViewModel(
        repository: RoutinesListRepository
    ): CreateRoutineViewModel {
        return CreateRoutineViewModel(repository)
    }
}
