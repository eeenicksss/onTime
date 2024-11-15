package com.example.ontime.routine.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.routine.domain.usecase.GetTasksUseCase
import com.example.ontime.routine.domain.usecase.SaveTasksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class RunningRoutineViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val saveTasksUseCase: SaveTasksUseCase,
    var uiState: RunningRoutineUiState
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
    fun getRoutineTotalTime(): Int {
        return uiState.tasks.sumOf {it.durationMins}
    }

    fun toggleTask(task: Task) {
        val updatedTask = task.copy(
            status = TaskStatus.entries[(task.status.ordinal + 1) % TaskStatus.entries.size])
        val updatedTasks = _tasks.value.map { if (it == task) updatedTask else it }
        saveTasks(updatedTasks)  // Сохраняем обновленный список задач
    }

    fun currentTask(): Task? {
        uiState.tasks.forEach() {
            if (it.status == TaskStatus.INCOMPLETED) return it
        }
        return null
    }

    fun toTimeString(hours: Int, minutes: Int): String {
        // Приводим часы и минуты к формату с ведущими нулями (например, 02:05)
        val hrs = hours.toString().padStart(2, '0')
        val mins = minutes.toString().padStart(2, '0')
        return "$hrs:$mins"
    }

    // Функция для преобразования времени из строки HH:MM в формат Hrs и Mins
    fun fromTimeString(time: String): Pair<Int, Int> {
        // Разделяем строку по двоеточию
        val parts = time.split(":")
        if (parts.size != 2) throw IllegalArgumentException("Invalid time format")
        val hours = parts[0].toInt()
        val minutes = parts[1].toInt()
        return hours to minutes
    }

    fun finishRoutine() {
        //TODO create finishing routine logic
    }

    fun currentTaskAction(status: TaskStatus) {
        uiState.tasks.forEach() {
            if (it.status == TaskStatus.INCOMPLETED) {
                it.status = status
                return
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    fun timePickerStateToLocalDateTime(timePickerState: TimePickerState): LocalDateTime {
        // Получаем сегодняшнюю дату в текущей временной зоне
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        // Создаем LocalDateTime с текущей датой и временем из TimePickerState
        return LocalDateTime(
            year = currentDate.year,
            monthNumber = currentDate.monthNumber,
            dayOfMonth = currentDate.dayOfMonth,
            hour = timePickerState.hour,
            minute = timePickerState.minute,
            second = 0,
            nanosecond = 0
        )
    }
}
