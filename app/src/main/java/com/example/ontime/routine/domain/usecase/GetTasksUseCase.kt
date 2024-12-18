package com.example.ontime.routine.domain.usecase

import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.presentation.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class GetTasksUseCase(val repository: RunningRoutineRepository, val routineId: String) {
    open suspend fun execute(): Flow<List<Task>> {
        return flow {
            emit(repository.getRoutineById(routineId))  // Загружаем задачи
        }
    }
}
