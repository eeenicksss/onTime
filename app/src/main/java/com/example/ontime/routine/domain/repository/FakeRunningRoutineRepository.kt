package com.example.ontime.routine.domain.repository

import com.example.ontime.routine.presentation.Task
import com.example.ontime.routine.presentation.TaskStatus

class FakeRunningRoutineRepository : RunningRoutineRepository {
    override suspend fun getRoutineById(routineId: String): List<Task> {
        // Возвращаем список задач для превью
        return listOf(
            Task("Очень длинная задача", durationMins = 1, status = TaskStatus.UNCOMPLETED),
            Task("Просто задача", durationMins = 8, status = TaskStatus.UNCOMPLETED),
            Task("Ну и последняя задача", durationMins = 10, status = TaskStatus.UNCOMPLETED),
            Task("Точно нет", durationMins = 3, status = TaskStatus.UNCOMPLETED),
            Task("Вот последняя задача", durationMins = 26, status = TaskStatus.UNCOMPLETED)
        )
    }

    override suspend fun saveTasks(tasks: List<Task>) {
        // Ничего не делаем для превью
    }
}
