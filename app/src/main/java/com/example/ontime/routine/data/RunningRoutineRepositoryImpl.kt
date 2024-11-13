package com.example.ontime.routine.data

import android.content.SharedPreferences
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.presentation.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RunningRoutineRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : RunningRoutineRepository {

    private val gson = Gson()

    override suspend fun saveTasks(tasks: List<Task>) {
        val tasksJson = gson.toJson(tasks)  // Сериализуем список задач в JSON
        sharedPreferences.edit().putString("tasks", tasksJson).apply()
    }

    override suspend fun loadTasks(): List<Task> {
        val tasksJson = sharedPreferences.getString("tasks", "[]")
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(tasksJson, type)
    }
}
