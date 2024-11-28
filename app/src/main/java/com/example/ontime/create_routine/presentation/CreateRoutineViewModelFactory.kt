package com.example.ontime.create_routine.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import javax.inject.Inject

class CreateRoutineViewModelFactory @Inject constructor(
    private val repository: RunningRoutineRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateRoutineViewModel::class.java)) {
            return CreateRoutineViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
