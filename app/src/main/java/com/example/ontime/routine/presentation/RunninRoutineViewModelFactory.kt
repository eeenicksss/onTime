package com.example.ontime.routine.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ontime.routine.domain.repository.FakeRunningRoutineRepository
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.domain.usecase.GetTasksUseCase
import com.example.ontime.routine.domain.usecase.SaveTasksUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RunningRoutineViewModelFactory @Inject constructor(
    private val repository: RunningRoutineRepository,
    private val dispatcher: CoroutineDispatcher,
    private val routineId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunningRoutineViewModel::class.java)) {
            Log.d("RunningRoutineViewModelFactory", "Initializing ViewModel with routine_id: $routineId")
            return RunningRoutineViewModel(repository, dispatcher, routineId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
