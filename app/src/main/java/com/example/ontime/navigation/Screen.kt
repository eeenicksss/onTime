package com.example.ontime.navigation

//sealed class Screen(val route: String) {
//    object CreateRoutine : Screen("create_routine")
//    object RunningRoutine : Screen("running_routine")
//    object RoutinesList : Screen("routines_list")
//}

object Screen {
    // Экран списка рутин
    val RoutinesList = "routines_list"

    // Экран создания рутины
    val CreateRoutine = "create_routine"

    // Экран выполнения рутины с параметром routine_id
    val RunningRoutine = "running_routine/{routine_id}"

    // Метод для динамического создания маршрута с параметром
    fun createRunningRoutineRoute(routineId: String) = "running_routine/$routineId"
}

