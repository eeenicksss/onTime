package com.example.ontime.navigation

sealed class Screen(val route: String) {
    object CreateRoutine : Screen("create_routine")
    object RunningRoutine : Screen("running_routine")
}