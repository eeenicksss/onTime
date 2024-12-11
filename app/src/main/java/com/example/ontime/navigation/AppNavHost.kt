package com.example.ontime.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ontime.create.presentation.CreateRoutineScreen
import com.example.ontime.create.presentation.CreateRoutineViewModel
import com.example.ontime.create.presentation.CreateRoutineViewModelFactory
import com.example.ontime.di.AppComponent
import com.example.ontime.routine.presentation.RunningRoutineScreen
import com.example.ontime.routine.presentation.RunningRoutineViewModel
import com.example.ontime.routine.presentation.RunningRoutineViewModelFactory
import com.example.ontime.list.presentation.RoutinesListScreen
import com.example.ontime.list.presentation.RoutinesListViewModel
import com.example.ontime.list.presentation.RoutinesListViewModelFactory

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.RoutinesList) {
        // Экран списка рутин
        composable(Screen.RoutinesList) {
            // Используем ViewModelProvider для создания RoutinesListViewModel с зависимостями через Dagger
            val routinesListViewModel: RoutinesListViewModel = viewModel(
                factory = RoutinesListViewModelFactory(
                    repository = AppComponent.instance.provideRoutinesListRepository()
                ) // Создаем ViewModel с помощью Factory
            )

//            RoutinesListScreen(
//                viewModel = routinesListViewModel,
//                onStartRoutineClick = {
//                    // Переход к экрану RunningRoutine с выбранной рутиной
//                    navController.navigate(Screen.RunningRoutine.route) {//"${Screen.RunningRoutine.route}/$routineId"
//                        // Включаем возможность возврата на экран списка рутин
//                        popUpTo(Screen.RoutinesList.route) { inclusive = true }
//                    }
//                },
//                onCreateRoutineClick = {
//                    // Переход к экрану создания рутины
//                    navController.navigate(Screen.CreateRoutine.route)
//                }
//            )
            RoutinesListScreen(
                viewModel = routinesListViewModel,
                onCreateRoutineClick = {
                    navController.navigate(Screen.CreateRoutine)
                },
                onStartRoutineClick = { routineId ->
                    Log.d("RoutinesListActivity", "Starting routine with ID: $routineId")
                    navController.navigate(Screen.createRunningRoutineRoute(routineId))
                }
            )
        }

        // Экран создания рутины
        composable(Screen.CreateRoutine) {
            // Используем ViewModelProvider для создания CreateRoutineViewModel с зависимостями через Dagger
            val createRoutineViewModel: CreateRoutineViewModel = viewModel(
                factory = CreateRoutineViewModelFactory(
                    repository = AppComponent.instance.provideRoutinesListRepository()
                ) // Создаем ViewModel с помощью Factory
            )

            CreateRoutineScreen(
                viewModel = createRoutineViewModel,
                onSaveClick = {
                    navController.navigate(Screen.RoutinesList) {
                        popUpTo(Screen.CreateRoutine) { inclusive = true }
                    }
                }
            )
        }

        // Экран выполнения рутины
        composable(Screen.RunningRoutine) {
            // Используем ViewModelProvider для создания RunningRoutineViewModel с зависимостями через Dagger
            val runningRoutineViewModel: RunningRoutineViewModel = viewModel(
                factory = RunningRoutineViewModelFactory(
                    repository = AppComponent.instance.provideRunningRoutineRepository(),
                    dispatcher = AppComponent.instance.provideCoroutineDispatcher(),
                    routineId = it.arguments?.getString("routine_id") ?: ""
                ) // Создаем ViewModel с помощью Factory
            )
            RunningRoutineScreen(viewModel = runningRoutineViewModel)
        }
    }
}
