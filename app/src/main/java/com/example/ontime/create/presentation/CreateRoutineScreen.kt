package com.example.ontime.create.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.ontime.R
import com.example.ontime.di.AppComponent
import com.example.ontime.list.presentation.RoutinesListActivity
import com.example.ontime.routine.presentation.Task
import com.example.ontime.routine.presentation.TaskStatus
import kotlinx.coroutines.launch

class CreateRoutineActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = AppComponent.instance.provideRoutinesListRepository()
        val viewModel = ViewModelProvider(
            this,
            CreateRoutineViewModelFactory(repository)
        )[CreateRoutineViewModel::class.java]

        setContent {
            CreateRoutineScreen(
                viewModel = viewModel,
                onSaveClick = {
                    startActivity(Intent(this, RoutinesListActivity::class.java))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoutineScreen(
    viewModel: CreateRoutineViewModel,
    onSaveClick: () -> Unit
) {
    val routineTitle by viewModel.routineTitle.collectAsState()
    val newTaskTitle by viewModel.newTaskTitle.collectAsState()
    val newTaskDuration by viewModel.newTaskDuration.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(colorResource(R.color.background))
            .padding(horizontal = 16.dp)
            .systemBarsPadding()
    ) {
        val outlinedTextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(R.color.green),
            focusedLabelColor = colorResource(R.color.light),
            focusedTextColor = colorResource(R.color.light),
            unfocusedBorderColor = colorResource(R.color.light),
            unfocusedLabelColor = colorResource(R.color.green),
            unfocusedTextColor = colorResource(R.color.light)
        )
        OutlinedTextField(
            value = routineTitle,
            onValueChange = { viewModel.updateRoutineTitle(it) },
            label = { Text(text = stringResource(id = R.string.routine_name))},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = outlinedTextFieldColors
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = newTaskTitle,
            onValueChange = { viewModel.updateTaskTitle(it) },
            label = { Text(text = stringResource(id = R.string.task_title)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = outlinedTextFieldColors
        )
        OutlinedTextField(
            value = newTaskDuration,
            onValueChange = { viewModel.updateTaskDuration(it) },
            label = { Text(text = stringResource(id = R.string.task_duration)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = outlinedTextFieldColors,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.addTask() },
            modifier = Modifier.height(52.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.green),
                contentColor = colorResource(R.color.on_green)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = stringResource(id = R.string.add_task))
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(tasks) { task ->
                Text(
                    text = "${task.title} - ${task.durationMins} min",
                    color = colorResource(R.color.light),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                    )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.saveRoutine(onSaveClick)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.green),
                contentColor = colorResource(R.color.on_green)
            ),
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(text = stringResource(id = R.string.save_routine))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateRoutineScreen() {
    // Фейковый ViewModel для превью
    val fakeViewModel = object : CreateRoutineViewModel(FakeRoutineListRepository()) {
        init {
            // Инициализация начальных значений
            updateRoutineTitle("Morning Routine")
            _tasks.value = listOf(
                Task(title = "Exercise", durationMins = 30, status = TaskStatus.UNCOMPLETED),
                Task(title = "Meditation", durationMins = 15, status = TaskStatus.UNCOMPLETED),
                Task(title = "Breakfast", durationMins = 20, status = TaskStatus.UNCOMPLETED)
            )
        }
    }

    CreateRoutineScreen(
        viewModel = fakeViewModel,
        onSaveClick = { /* No-op for preview */ }
    )
}