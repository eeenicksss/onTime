package com.example.ontime.routine.domain.repository

import com.example.ontime.routine.presentation.Task
import com.example.ontime.routine.presentation.TaskStatus

class FakeRunningRoutineRepository : RunningRoutineRepository {
    override suspend fun loadTasks(): List<Task> {
        // Возвращаем список задач для превью
        return listOf(
            Task("Очень длинная задача", durationMins = 1, status = TaskStatus.INCOMPLETED),
            Task("Просто задача", durationMins = 8, status = TaskStatus.INCOMPLETED),
            Task("Ну и последняя задача", durationMins = 10, status = TaskStatus.INCOMPLETED),
            Task("Точно нет", durationMins = 3, status = TaskStatus.INCOMPLETED),
            Task("Вот последняя задача", durationMins = 26, status = TaskStatus.INCOMPLETED)
        )
    }

    override suspend fun saveTasks(tasks: List<Task>) {
        // Ничего не делаем для превью
    }
}
