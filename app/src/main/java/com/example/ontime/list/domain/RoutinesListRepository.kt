package com.example.ontime.list.domain

import com.example.ontime.list.presentation.Routine

interface RoutinesListRepository {
    suspend fun getAllRoutines(): List<Routine>     // Получить все рутины
    suspend fun saveRoutine(routine: Routine)       // Сохранить рутину
    suspend fun deleteRoutine(routineId: String)    // Удалить рутину по ID
}
