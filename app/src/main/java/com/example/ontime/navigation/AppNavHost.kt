package com.example.ontime.navigation

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
            val routinesListViewModel: RoutinesListViewModel = viewModel(
                factory = RoutinesListViewModelFactory(
                    repository = AppComponent.instance.provideRoutinesListRepository()
                )
            )

            RoutinesListScreen(
                viewModel = routinesListViewModel,
                onStartRoutineClick = { routineId ->
                    navController.navigate(Screen.createRunningRoutineRoute(routineId)) // Переход с передачей параметра
                },
                onCreateRoutineClick = {
                    navController.navigate(Screen.CreateRoutine)
                }
            )
        }

        // Экран создания рутины
        composable(Screen.CreateRoutine) {
            val createRoutineViewModel: CreateRoutineViewModel = viewModel(
                factory = CreateRoutineViewModelFactory(
                    repository = AppComponent.instance.provideRoutinesListRepository()
                )
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

        // Экран выполнения рутины с параметром routine_id
        composable(Screen.RunningRoutine) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getString("routine_id") ?: ""
            val runningRoutineViewModel: RunningRoutineViewModel = viewModel(
                factory = RunningRoutineViewModelFactory(
                    repository = AppComponent.instance.provideRunningRoutineRepository(),
                    dispatcher = AppComponent.instance.provideCoroutineDispatcher(),
                    routineId = routineId
                )
            )
            RunningRoutineScreen(
                viewModel = runningRoutineViewModel,
                onFinishClick = {
                    navController.navigate(Screen.RoutinesList) {
                }}
            )
        }
    }
}
