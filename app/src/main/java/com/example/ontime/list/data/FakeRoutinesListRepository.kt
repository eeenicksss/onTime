package com.example.ontime.list.data

import com.example.ontime.list.domain.RoutinesListRepository
import com.example.ontime.list.presentation.Routine
import com.example.ontime.routine.presentation.Task
import com.example.ontime.routine.presentation.TaskStatus

class FakeRoutinesListRepository(): RoutinesListRepository {
    private val routines = mutableListOf<Routine>()

    override suspend fun getAllRoutines(): List<Routine> {
        return routines.toList() // Возвращаем копию списка, чтобы избежать изменений извне
    }

    override suspend fun saveRoutine(routine: Routine) {
        routines.add(routine)
    }

    override suspend fun deleteRoutine(routineId: String) {
        routines.removeIf { it.id == routineId }
    }
}