package com.example.ontime.create_routine.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.presentation.Task
import com.example.ontime.routine.presentation.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateRoutineViewModel(
    private val repository: RunningRoutineRepository // Добавляем репозиторий в конструктор
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateRoutineUIState())
    val uiState: StateFlow<CreateRoutineUIState> = _uiState

    fun onEvent(event: CreateRoutineEvents) {
        when (event) {
            is CreateRoutineEvents.UpdateTaskTitle -> {
                _uiState.value = _uiState.value.copy(newTaskTitle = event.title)
            }
            is CreateRoutineEvents.UpdateTaskDuration -> {
                _uiState.value = _uiState.value.copy(newTaskDuration = event.duration)
            }
            CreateRoutineEvents.AddTask -> {
                val duration = _uiState.value.newTaskDuration.toIntOrNull()
                if (duration != null && duration > 0) {
                    val newTask = Task(
                        title = _uiState.value.newTaskTitle,
                        durationMins = duration,
                        status = TaskStatus.INCOMPLETED
                    )
                    _uiState.value = _uiState.value.copy(
                        tasks = _uiState.value.tasks + newTask,
                        newTaskTitle = "",
                        newTaskDuration = ""
                    )
                }
            }
            CreateRoutineEvents.SubmitRoutine -> {
                // Сохраняем задачи в репозиторий
                val tasks = _uiState.value.tasks
                viewModelScope.launch {
                    repository.saveTasks(tasks)  // Сохраняем задачи в репозиторий
                }
                // TODO: Реализуйте переход к RunningRoutineScreen
            }
        }
    }
}
