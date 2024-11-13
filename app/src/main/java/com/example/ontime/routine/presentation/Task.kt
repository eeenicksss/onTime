package com.example.ontime.routine.presentation

data class Task (
    val title: String,
    val durationMins: Int,
    val status: TaskStatus
)
