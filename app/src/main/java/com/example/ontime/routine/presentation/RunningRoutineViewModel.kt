package com.example.ontime.routine.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.R
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class RunningRoutineViewModel @Inject constructor(
    private val repository: RunningRoutineRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _currentTask = MutableStateFlow<Task?>(null)
    val currentTask = _currentTask.asStateFlow()

    private val _startTime = MutableStateFlow(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()))
    val startTime = _startTime.asStateFlow()

    private val _secondsElapsed = MutableStateFlow(0)
    val secondsElapsed = _secondsElapsed.asStateFlow()

    private val _accentColorIdPair = MutableStateFlow(R.color.gray to R.color.light)
    val accentColorIdPair = _accentColorIdPair.asStateFlow()

    init {
        viewModelScope.launch(dispatcher) { // Используем заданный диспетчер
            loadTasks()
            setCurrentTask()
            startTimer()
        }
    }

    private suspend fun loadTasks() {
        _tasks.value = repository.loadTasks()
    }

    private fun startTimer() {
        val startTime = startTime.value.toInstant(TimeZone.currentSystemDefault())
        viewModelScope.launch(dispatcher) {
            while(true) {
                val elapsedMillis = Clock.System.now().toEpochMilliseconds() - startTime.toEpochMilliseconds()
                _secondsElapsed.value = (elapsedMillis / 1000).toInt()
                setAccentColorIdPair()
                delay(1000L)
            }
        }
    }

    private fun saveTasksToRepository(tasks: List<Task>) {
        viewModelScope.launch {
            repository.saveTasks(tasks)
        }
    }
    fun getRoutineTotalTime(): Int {
        return tasks.value.sumOf {it.durationMins}
    }

    fun toggleTask(task: Task) {
        val newStatus = TaskStatus.entries[(task.status.ordinal + 1) % TaskStatus.entries.size]
        setTaskStatus(task, newStatus)
    }

    fun setTaskStatus(task: Task, status: TaskStatus) {
        _tasks.value = _tasks.value.map {
            if (it == task) it.copy(status = status) else it
        }
        setCurrentTask()
        setAccentColorIdPair()
        saveTasksToRepository(tasks.value)
    }

    private fun setCurrentTask() {
        val currentTask = tasks.value.find { it.status == TaskStatus.UNCOMPLETED }
        _currentTask.value = currentTask
    }

    private fun getAnticipatedTime(): Int {
        var time = 0
        tasks.value.forEach { task ->
            if (task.status != TaskStatus.UNCOMPLETED) {
                time += task.durationMins
            }
        }
        return time
    }

    private fun setAccentColorIdPair() {
        val anticipatedTime = getAnticipatedTime() * 60
        val currentTaskTime = currentTask.value?.durationMins?.let{it * 60} ?: 0
        _accentColorIdPair.value = when {
            secondsElapsed.value <= anticipatedTime -> R.color.green to R.color.on_green
            secondsElapsed.value <= anticipatedTime + currentTaskTime -> R.color.gray to R.color.light
            else -> R.color.red to R.color.light
        }
    }

    fun updateStartTime(newStartTime: LocalDateTime) {
        _startTime.value = newStartTime
    }

    fun toTimeString(hours: Int, minutes: Int): String {
        val hrs = hours.toString().padStart(2, '0')
        val mins = minutes.toString().padStart(2, '0')
        return "$hrs:$mins"
    }

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
