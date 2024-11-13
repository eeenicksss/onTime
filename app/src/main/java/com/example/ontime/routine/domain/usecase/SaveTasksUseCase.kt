package com.example.ontime.routine.domain.usecase

import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.presentation.Task

class SaveTasksUseCase(private val repository: RunningRoutineRepository) {
    suspend fun execute(tasks: List<Task>) {
        repository.saveTasks(tasks)  // Сохраняем задачи
    }
}
