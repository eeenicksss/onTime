package com.example.ontime.create_routine.presentation

sealed class CreateRoutineEvents {
    data class UpdateTaskTitle(val title: String) : CreateRoutineEvents()
    data class UpdateTaskDuration(val duration: String) : CreateRoutineEvents()
    object AddTask : CreateRoutineEvents()
    object SubmitRoutine : CreateRoutineEvents()
}
