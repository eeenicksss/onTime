package com.example.ontime.create_routine.presentation

import com.example.ontime.routine.presentation.Task

data class CreateRoutineUIState(
    val tasks: List<Task> = listOf(),
    val newTaskTitle: String = "",
    val newTaskDuration: String = "" // Сохраняется в виде строки для обработки ввода
)