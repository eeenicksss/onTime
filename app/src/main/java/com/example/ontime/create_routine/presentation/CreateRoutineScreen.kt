package com.example.ontime.create_routine.presentation


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ontime.R
import com.example.ontime.routine.presentation.Task

@Composable
fun CreateRoutineScreen(
    viewModel: CreateRoutineViewModel,
    onRoutineSubmitted: (List<Task>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier
        .background(colorResource(R.color.background))
        .fillMaxSize()
        .padding(16.dp)
        .systemBarsPadding()
    ) {
        TextField(
            value = uiState.newTaskTitle,
            onValueChange = { viewModel.onEvent(CreateRoutineEvents.UpdateTaskTitle(it)) },
            label = { Text("Task Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = uiState.newTaskDuration,
            onValueChange = { viewModel.onEvent(CreateRoutineEvents.UpdateTaskDuration(it)) },
            label = { Text("Task Duration (mins)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.onEvent(CreateRoutineEvents.AddTask) }) {
            Text("Add Task")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(uiState.tasks) { task ->
                Text("${task.title} - ${task.durationMins} mins")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.onEvent(CreateRoutineEvents.SubmitRoutine)
                onRoutineSubmitted(uiState.tasks)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Submit Routine")
        }
    }
}
