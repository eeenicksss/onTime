package com.example.ontime.routine.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ontime.routine.domain.usecase.GetTasksUseCase
import com.example.ontime.routine.domain.usecase.SaveTasksUseCase
import javax.inject.Inject

class RunningRoutineViewModelFactory @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val saveTasksUseCase: SaveTasksUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunningRoutineViewModel::class.java)) {
            return RunningRoutineViewModel(getTasksUseCase, saveTasksUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
