package com.example.ontime.routine.data

import android.content.SharedPreferences
import android.util.Log
import com.example.ontime.list.presentation.Routine
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.presentation.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RunningRoutineRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : RunningRoutineRepository {

    private val gson = Gson()
    private val type = object : TypeToken<List<Routine>>() {}.type

    override suspend fun saveTasks(tasks: List<Task>) {
        val tasksJson = gson.toJson(tasks)  // Сериализуем список задач в JSON
        sharedPreferences.edit().putString("tasks", tasksJson).apply()
    }

    override suspend fun getRoutineById(routineId: String): List<Task> {
        val routinesJson = sharedPreferences.getString("routines", "[]")
        val routines = gson.fromJson<List<Routine>>(routinesJson, type)
        val routine = routines.find { it.id == routineId }?.tasks ?: listOf()
        Log.d("RunningRoutineRepository", "Fetched routine: $routine for id: $routineId")
        return routine
    }
}
