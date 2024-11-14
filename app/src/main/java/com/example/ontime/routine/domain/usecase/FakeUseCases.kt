package com.example.ontime.routine.domain.usecase


import com.example.ontime.routine.domain.repository.FakeRunningRoutineRepository
import com.example.ontime.routine.presentation.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeGetTasksUseCase : GetTasksUseCase(FakeRunningRoutineRepository()) {
    override suspend fun execute(): Flow<List<Task>> {
        return flow {
            emit(repository.loadTasks())  // Загружаем фиктивные задачи
        }
    }
}

class FakeSaveTasksUseCase : SaveTasksUseCase(FakeRunningRoutineRepository()) {
    override suspend fun execute(tasks: List<Task>) {
        // Ничего не делаем для превью
    }
}
