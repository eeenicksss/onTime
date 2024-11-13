package com.example.ontime.routine.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.routine.domain.usecase.GetTasksUseCase
import com.example.ontime.routine.domain.usecase.SaveTasksUseCase
import kotlinx.coroutines.launch

class RunningRoutineViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val saveTasksUseCase: SaveTasksUseCase
) : ViewModel() {

    private val _tasks = mutableStateOf<List<Task>>(emptyList())
    val tasks = _tasks

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
        }
    }

    fun toggleTask(task: Task) {
        val updatedTask = task.copy(
            status = TaskStatus.entries[(task.status.ordinal + 1) % TaskStatus.entries.size])
        val updatedTasks = _tasks.value.map { if (it == task) updatedTask else it }
        saveTasks(updatedTasks)  // Сохраняем обновленный список задач
    }
}
