package com.example.ontime.routine.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import java.lang.reflect.Modifier

@Composable
fun RunningRoutineScreen(
    modifier: Modifier = Modifier(),
    viewModel: RunningRoutineViewModel = RunningRoutineViewModel()
) {
    LaunchedEffect(key1 = viewModel.runningRoutineEventsFlow) {
        viewModel.runningRoutineEventsFlow.collect { event ->
          when(event) {
              RunningRoutineEvents.SomeEvent -> TODO()
          }
        }
    }
}