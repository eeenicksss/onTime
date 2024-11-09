package com.example.ontime.routine.presentation

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class RunningRoutineUiState (
    val startTime: Instant = Clock.System.now(),
    val tasks: List<Task> = listOf()
)
