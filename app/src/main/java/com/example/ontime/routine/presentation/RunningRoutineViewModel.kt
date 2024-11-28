package com.example.ontime.routine.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.R
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class RunningRoutineViewModel @Inject constructor(
    private val repository: RunningRoutineRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(RunningRoutineUiState())
    val uiState: StateFlow<RunningRoutineUiState> = _uiState

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        viewModelScope.launch(dispatcher) { // Используем заданный диспетчер
            loadTasks()
            _uiState.value = _uiState.value.copy(
                tasks = _tasks.value,
                currentTask = getCurrentTask()
            )
        }
    }

    private suspend fun loadTasks() {
        val loadedTasks = repository.loadTasks()
        _tasks.value = loadedTasks
        _uiState.value = _uiState.value.copy(tasks = loadedTasks, currentTask = getCurrentTask())
    }

    private fun saveTasks(tasks: List<Task>) {
        viewModelScope.launch {
            repository.saveTasks(tasks)
            _tasks.value = tasks
        }
    }
    fun getRoutineTotalTime(): Int {
        return uiState.value.tasks.sumOf {it.durationMins}
    }

    fun toggleTask(task: Task) {
        val updatedTask = task.copy(
            status = TaskStatus.entries[(task.status.ordinal + 1) % TaskStatus.entries.size])
        val updatedTasks = _tasks.value.map { if (it == task) updatedTask else it }
        saveTasks(updatedTasks)  // Сохраняем обновленный список задач

        _uiState.value = _uiState.value.copy(
            tasks = updatedTasks
        )
        saveTasks(updatedTasks)
        _uiState.value = _uiState.value.copy(
            currentTask = getCurrentTask()
        )
    }

    private fun getCurrentTask(): Task? {
        return uiState.value.tasks.find { it.status == TaskStatus.INCOMPLETED }
    }

    private fun getAnticipatedTime(): Int {
        var time = 0
        uiState.value.tasks.forEach() {
            if (it.status != TaskStatus.INCOMPLETED) time += it.durationMins
        }
        return time
    }

    fun getAccentColorIdPair(startTime: Instant): Pair<Int, Int> {
        val anticipatedTime = getAnticipatedTime()
        val elapsedTime = ((Clock.System.now().toEpochMilliseconds() - startTime.toEpochMilliseconds()) / 1000).toInt()
        val currentTaskTime = uiState.value.currentTask?.durationMins ?: 0
        return when {
            elapsedTime <= anticipatedTime -> R.color.green to R.color.on_green
            elapsedTime <= anticipatedTime + currentTaskTime -> R.color.gray to R.color.light
            else -> R.color.red to R.color.light
        }
    }

    fun updateStartTime(newStartTime: LocalDateTime) {
        _uiState.value = _uiState.value.copy(startTime = newStartTime)
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

    fun setCurrentTaskStatus(status: TaskStatus) {
        uiState.value.tasks.forEach() {
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
