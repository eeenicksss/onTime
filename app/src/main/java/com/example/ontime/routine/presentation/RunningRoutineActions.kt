package com.example.ontime.routine.presentation

import kotlinx.datetime.Instant

sealed interface RunningRoutineActions {
    data class CompleteTask(val taskList: List<Task>) : RunningRoutineActions
    data class ChangeStartTime(val time: Instant): RunningRoutineActions
    data class ChangeEndTime(val time: Instant): RunningRoutineActions
}