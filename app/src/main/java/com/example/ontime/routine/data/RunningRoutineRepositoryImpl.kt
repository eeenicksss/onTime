package com.example.ontime.routine.data

import android.content.SharedPreferences
import com.example.ontime.routine.domain.repository.RunningRoutineRepository

class RunningRoutineRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
): RunningRoutineRepository {
}