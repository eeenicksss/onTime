package com.example.ontime.routine.presentation

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
    private val dispatcher: CoroutineDispatcher
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RunningRoutineViewModel::class.java)) {
            return RunningRoutineViewModel(repository, dispatcher) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
