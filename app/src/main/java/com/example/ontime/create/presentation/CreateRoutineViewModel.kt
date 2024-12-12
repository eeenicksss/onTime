package com.example.ontime.create.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.list.domain.RoutinesListRepository
import com.example.ontime.list.presentation.Routine
import com.example.ontime.routine.presentation.Task
import com.example.ontime.routine.presentation.TaskStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class CreateRoutineViewModel(
    private val repository: RoutinesListRepository
) : ViewModel() {

    private val _routineTitle = MutableStateFlow("")
    val routineTitle: StateFlow<String> = _routineTitle

    private val _newTaskTitle = MutableStateFlow("")
    val newTaskTitle: StateFlow<String> = _newTaskTitle

    private val _newTaskDuration = MutableStateFlow("")
    val newTaskDuration: StateFlow<String> = _newTaskDuration

    val _tasks = MutableStateFlow(emptyList<Task>())
    val tasks: StateFlow<List<Task>> = _tasks

    fun updateTaskTitle(title: String) {
        _newTaskTitle.value = title
    }
    fun updateTaskDuration(duration: String) {
        _newTaskDuration.value = duration
    }
    fun updateRoutineTitle(title: String) {
        _routineTitle.value = title
    }
    fun addTask() {
        val duration = _newTaskDuration.value.toIntOrNull()
        if (
            duration != null &&
            duration > 0 &&
            duration <= 120 && // 2 hours limit per task
            _newTaskTitle.value.isNotBlank() &&
            tasks.value.sumOf{it.durationMins} + duration <= 1440 //24 hour maximum length of routine
            ) {
            val newTask = Task(
                title = _newTaskTitle.value,
                durationMins = duration,
                status = TaskStatus.UNCOMPLETED
            )
            _tasks.value += newTask
            _newTaskTitle.value = ""
            _newTaskDuration.value = ""
        } //TODO Add else branch with signalization that duration or title is not valid
    }
    fun saveRoutine(intent: ()-> Unit) {
        if (_routineTitle.value.isNotBlank() && _tasks.value.isNotEmpty()) {
            val routine = Routine(
                id = generateRoutineId(),
                title = _routineTitle.value,
                tasks = _tasks.value
            )
            viewModelScope.launch {
                repository.saveRoutine(routine) // Сохраняем рутину
            }
            intent()
        }
    }
    // Генерация уникального ID для рутины
    private fun generateRoutineId(): String {
        return "routine_${System.currentTimeMillis()}"
    }
}
