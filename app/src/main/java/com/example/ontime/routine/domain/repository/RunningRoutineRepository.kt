package com.example.ontime.routine.domain.repository

import com.example.ontime.routine.presentation.Task

interface RunningRoutineRepository {
    suspend fun saveTasks(tasks: List<Task>)
    suspend fun getRoutineById(routineId: String): List<Task>
}