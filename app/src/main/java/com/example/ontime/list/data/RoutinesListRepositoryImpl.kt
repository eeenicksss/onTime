package com.example.ontime.list.data

import com.example.ontime.list.domain.RoutinesListRepository
import android.content.SharedPreferences
import com.example.ontime.list.presentation.Routine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoutinesListRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : RoutinesListRepository {

    private val gson = Gson()

    override suspend fun getAllRoutines(): List<Routine> {
        val routinesJson = sharedPreferences.getString("routines", "[]")
        val type = object : TypeToken<List<Routine>>() {}.type
        return gson.fromJson(routinesJson, type)
    }

    override suspend fun saveRoutine(routine: Routine) {
        val currentRoutines = getAllRoutines().toMutableList()
        currentRoutines.add(routine)
        saveAllRoutines(currentRoutines)
    }

    override suspend fun deleteRoutine(routineId: String) {
        val currentRoutines = getAllRoutines().toMutableList()
        val routineToDelete = currentRoutines.find { it.id == routineId }
        routineToDelete?.let {
            currentRoutines.remove(it)
            saveAllRoutines(currentRoutines)
        }
    }

    private suspend fun saveAllRoutines(routines: List<Routine>) {
        val routinesJson = gson.toJson(routines)
        sharedPreferences.edit().putString("routines", routinesJson).apply()
    }
}
