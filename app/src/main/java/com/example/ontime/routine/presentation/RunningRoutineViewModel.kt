package com.example.ontime.routine.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import javax.inject.Inject

class RunningRoutineViewModel @Inject constructor(
    private val repository: RunningRoutineRepository
) : ViewModel() {

    var state by mutableStateOf(RunningRoutineUiState())
        private set

    private val _runningRoutineChannel = Channel<RunningRoutineEvents>()
    val runningRoutineEventsFlow = _runningRoutineChannel.receiveAsFlow()

    private val routineDuration: Int
        get() {
            return state.tasks.sumOf {it.durationMins}
        }

    fun onAction(action: RunningRoutineActions) {
        state = when(action) {
            is RunningRoutineActions.CompleteTask -> {
                state.copy(tasks = action.taskList)
            }

            is RunningRoutineActions.ChangeStartTime -> {
                state.copy(startTime = action.time)
            }

            is RunningRoutineActions.ChangeEndTime -> {
                state.copy(startTime = action.time.minus(routineDuration, DateTimeUnit.MINUTE))
            }
        }
    }

    fun completeTask(){
        viewModelScope.launch{
            _runningRoutineChannel.send(RunningRoutineEvents.SomeEvent)
        }
    }
}