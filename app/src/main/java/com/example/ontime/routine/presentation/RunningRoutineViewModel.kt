package com.example.ontime.routine.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.routine.domain.usecase.GetTasksUseCase
import com.example.ontime.routine.domain.usecase.SaveTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RunningRoutineViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val saveTasksUseCase: SaveTasksUseCase
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            getTasksUseCase.execute().collect { tasks ->
                _tasks.value = tasks  // Загружаем задачи в состояние
            }
        }
    }

    fun saveTasks(tasks: List<Task>) {
        viewModelScope.launch {
            saveTasksUseCase.execute(tasks)  // Сохраняем задачи
            _tasks.value = tasks
        }
    }

    fun toggleTask(task: Task) {
        val updatedTask = task.copy(
            status = TaskStatus.entries[(task.status.ordinal + 1) % TaskStatus.entries.size])
        val updatedTasks = _tasks.value.map { if (it == task) updatedTask else it }
        saveTasks(updatedTasks)  // Сохраняем обновленный список задач
    }
}
