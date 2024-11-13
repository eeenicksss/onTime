package com.example.ontime.routine.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.ontime.routine.data.RunningRoutineRepositoryImpl
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import java.lang.reflect.Modifier

@Composable
fun RunningRoutineScreen(
    modifier: Modifier = Modifier(),
    viewModel: RunningRoutineViewModel = RunningRoutineViewModel(RunningRoutineRepositoryImpl())
) {
    LaunchedEffect(key1 = viewModel.runningRoutineEventsFlow) {
        viewModel.runningRoutineEventsFlow.collect { event ->
          when(event) {
              RunningRoutineEvents.SomeEvent -> TODO()
          }
        }
    }
}