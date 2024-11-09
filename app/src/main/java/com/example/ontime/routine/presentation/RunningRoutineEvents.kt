package com.example.ontime.routine.presentation

sealed interface RunningRoutineEvents {
    data object SomeEvent: RunningRoutineEvents
}