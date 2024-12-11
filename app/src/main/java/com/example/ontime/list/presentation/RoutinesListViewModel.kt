package com.example.ontime.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.list.domain.RoutinesListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoutinesListViewModel(
    private val repository: RoutinesListRepository
) : ViewModel() {

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines: StateFlow<List<Routine>> = _routines

    init {
        loadRoutines()
    }

    private fun loadRoutines() {
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
