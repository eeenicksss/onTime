package com.example.ontime.create.presentation

import com.example.ontime.list.domain.RoutinesListRepository
import com.example.ontime.list.presentation.Routine

class FakeRoutineListRepository(): RoutinesListRepository {
    override suspend fun getAllRoutines(): List<Routine> {
        TODO("Not yet implemented")
    }

    override suspend fun saveRoutine(routine: Routine) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRoutine(routineId: String) {
        TODO("Not yet implemented")
    }
}