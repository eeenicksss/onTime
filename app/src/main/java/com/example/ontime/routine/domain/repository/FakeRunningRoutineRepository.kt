package com.example.ontime.routine.domain.repository

import com.example.ontime.routine.presentation.Task
import com.example.ontime.routine.presentation.TaskStatus

class FakeRunningRoutineRepository : RunningRoutineRepository {
    override suspend fun loadTasks(): List<Task> {
        // Возвращаем список задач для превью
        return listOf(
            Task("Task 1", 23, TaskStatus.INCOMPLETED),
            Task("Task 2", 17,  TaskStatus.INCOMPLETED),
            Task("Task 3", 45, TaskStatus.SKIPPED)
        )
    }

    override suspend fun saveTasks(tasks: List<Task>) {
        // Ничего не делаем для превью
    }
}
