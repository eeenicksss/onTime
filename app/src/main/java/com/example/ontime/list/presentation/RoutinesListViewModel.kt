package com.example.ontime.list.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.list.domain.RoutinesListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Modifier.PRIVATE

class RoutinesListViewModel(
    private val repository: RoutinesListRepository
) : ViewModel() {

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines: StateFlow<List<Routine>> = _routines

    init {
        loadRoutines()
    }

    @VisibleForTesting(otherwise = PRIVATE)
    fun loadRoutines() {
        viewModelScope.launch {
            _routines.value = repository.getAllRoutines()
        }
    }

    fun deleteRoutine(routineId: String) {
        viewModelScope.launch {
            repository.deleteRoutine(routineId)
            loadRoutines() // Refresh the list
        }
    }
}
