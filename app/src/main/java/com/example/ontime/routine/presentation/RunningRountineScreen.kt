package com.example.ontime.routine.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.ontime.core.ui.theme.OnTimeTheme
import com.example.ontime.di.AppComponent
import com.example.ontime.routine.domain.usecase.FakeGetTasksUseCase
import com.example.ontime.routine.domain.usecase.FakeSaveTasksUseCase
import com.example.ontime.routine.domain.usecase.GetTasksUseCase
import com.example.ontime.routine.domain.usecase.SaveTasksUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RunningRoutineActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Создаём экземпляр фабрики
        val factory = RunningRoutineViewModelFactory(
            getTasksUseCase = AppComponent.instance.provideGetTasksUseCase(),
            saveTasksUseCase = AppComponent.instance.provideSaveTasksUseCase()
        )

        // Инжектируем зависимости в фабрику
        AppComponent.instance.inject(factory)
        val viewModel = ViewModelProvider(this, factory).get(RunningRoutineViewModel::class.java)

        setContent {
            RunningRoutineScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun RunningRoutineScreen(
    modifier: Modifier = Modifier,
    viewModel: RunningRoutineViewModel
) {
    val tasks = viewModel.tasks.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        tasks.value.forEach { task ->
            TaskItem(task = task, onTaskClick = { viewModel.toggleTask(it) })
        }
    }
}

@Composable
fun TaskItem(task: Task, onTaskClick: (Task) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTaskClick(task) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicText(
            text = task.title,
            modifier = Modifier.weight(1f)
        )

        Button(onClick = { onTaskClick(task) }) {
            Text(text = task.status.name)  // Показываем статус задачи
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RunningRoutineScreenPreview() {
    OnTimeTheme {
        RunningRoutineScreen(viewModel = previewRunningRoutineViewModel())
    }
}

@Composable
fun previewRunningRoutineViewModel(): RunningRoutineViewModel {
    // Используем фиктивные реализации use-case
    return RunningRoutineViewModel(
        getTasksUseCase = FakeGetTasksUseCase(),
        saveTasksUseCase = FakeSaveTasksUseCase()
    )
}