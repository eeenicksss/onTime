package com.example.ontime.di

import com.example.ontime.list.domain.RoutinesListRepository
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.domain.usecase.GetTasksUseCase
import com.example.ontime.routine.domain.usecase.SaveTasksUseCase
import com.example.ontime.routine.presentation.RunningRoutineViewModelFactory
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(factory: RunningRoutineViewModelFactory)

    //fun provideGetTasksUseCase(): GetTasksUseCase
    //fun provideSaveTasksUseCase(): SaveTasksUseCase
    fun provideRunningRoutineRepository(): RunningRoutineRepository
    fun provideCoroutineDispatcher(): CoroutineDispatcher
    fun provideRoutinesListRepository(): RoutinesListRepository

//    @Component.Builder
//    interface Builder {
//        fun appModule(appModule: AppModule): Builder
//        fun build(): AppComponent
//    }

    companion object {
        lateinit var instance: AppComponent
    }
}