package com.example.ontime.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ontime.create_routine.presentation.CreateRoutineScreen
import com.example.ontime.create_routine.presentation.CreateRoutineViewModel
import com.example.ontime.create_routine.presentation.CreateRoutineViewModelFactory
import com.example.ontime.di.AppComponent
import com.example.ontime.routine.presentation.RunningRoutineScreen
import com.example.ontime.routine.presentation.RunningRoutineViewModel
import com.example.ontime.routine.presentation.RunningRoutineViewModelFactory

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.CreateRoutine.route) {
        composable(Screen.CreateRoutine.route) {
            // Используем ViewModelProvider для создания CreateRoutineViewModel с зависимостями через Dagger
            val createRoutineViewModel: CreateRoutineViewModel = viewModel(
                factory = CreateRoutineViewModelFactory(
                    repository = AppComponent.instance.provideRunningRoutineRepository()
                ) // Создаем ViewModel с помощью Factory
            )

            CreateRoutineScreen(
                viewModel = createRoutineViewModel,
                onRoutineSubmitted = { tasks ->
                    // Переход к экрану RunningRoutine с переданными задачами
                    navController.navigate(Screen.RunningRoutine.route) {
                        popUpTo(Screen.CreateRoutine.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.RunningRoutine.route) {
            // Используем ViewModelProvider для создания RunningRoutineViewModel с зависимостями через Dagger
            val runningRoutineViewModel = viewModel<RunningRoutineViewModel>(
                factory = RunningRoutineViewModelFactory(
                    repository = AppComponent.instance.provideRunningRoutineRepository(),
                    dispatcher = AppComponent.instance.provideCoroutineDispatcher()
                ) // Создаем ViewModel с помощью Factory
            )
            RunningRoutineScreen(viewModel = runningRoutineViewModel)
        }
    }
}
