package com.example.ontime.list.presentation

import com.example.ontime.routine.presentation.Task

data class Routine(
    val id: String, // Unique identifier for the routine
    val title: String,
    val tasks: List<Task>
)
