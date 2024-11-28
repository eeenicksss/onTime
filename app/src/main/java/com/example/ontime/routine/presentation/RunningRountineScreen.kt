package com.example.ontime.routine.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.ontime.R
import com.example.ontime.core.ui.theme.OnTimeTheme
import com.example.ontime.di.AppComponent
import com.example.ontime.routine.domain.repository.FakeRunningRoutineRepository
import com.example.ontime.routine.domain.usecase.FakeGetTasksUseCase
import com.example.ontime.routine.domain.usecase.FakeSaveTasksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class RunningRoutineActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Получаем зависимости из AppComponent
        val repository = AppComponent.instance.provideRunningRoutineRepository()
        val dispatcher = AppComponent.instance.provideCoroutineDispatcher()

        // Создаём экземпляр фабрики
        val factory = RunningRoutineViewModelFactory(
            repository = repository,
            dispatcher = dispatcher
        )

        // Инжектируем зависимости в фабрику
        AppComponent.instance.inject(factory)
        val viewModel = ViewModelProvider(this, factory).get(RunningRoutineViewModel::class.java)

        setContent {
            RunningRoutineScreen(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunningRoutineScreen(viewModel: RunningRoutineViewModel) {
    val tasks by viewModel.tasks.collectAsState() // Observing tasks state
    val uiState by viewModel.uiState.collectAsState()

    val startTime = uiState.startTime
    //var startTime by remember { mutableStateOf(viewModel.uiState.startTime)}
    var showStartTimePicker by remember { mutableStateOf(false)}
    val accentColorPair = viewModel.getAccentColorIdPair(
        startTime.toInstant(TimeZone.currentSystemDefault())
    ).let { colorResource(id = it.first) to colorResource(id = it.second) }


    // Main layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TimeBar(
            startTime = startTime.toInstant(TimeZone.currentSystemDefault()),
            totalTime = viewModel.getRoutineTotalTime(),
            accentColorPair = accentColorPair
        )

        if (showStartTimePicker) {
            StartTimePicker(
                onConfirm = {selectedTime ->
                    viewModel.updateStartTime(viewModel.timePickerStateToLocalDateTime(selectedTime))
                },
                onDismiss = { showStartTimePicker = false },
                initialHour = startTime.hour,
                initialMinute = startTime.minute
            )
        }

        // Task List
        TaskList(
            modifier = Modifier.weight(1f),
            tasks = tasks,
            onToggleTask = { task ->
                viewModel.toggleTask(task)
            }
        )
        BottomAction(
            task = uiState.currentTask,
            accentColorPair = accentColorPair,
            onFinishClick = { viewModel.finishRoutine() },
            onSkipClick = { viewModel.setCurrentTaskStatus(TaskStatus.SKIPPED) },
            //onCompleteClick = { viewModel.setCurrentTaskStatus(TaskStatus.COMPLETED) },
            onCompleteClick = {
                if (uiState.currentTask != null) viewModel.toggleTask(uiState.currentTask!!)
            }
        )
    }
}

@Composable
fun TaskList(modifier: Modifier = Modifier, tasks: List<Task>, onToggleTask: (Task) -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(tasks) { task ->
            TaskItem(task = task, onToggleTask = onToggleTask)
        }
    }
}

@Composable
fun TaskItem(task: Task, onToggleTask: (Task) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 5.dp)
            .clickable { onToggleTask(task) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val textColor = if (task.status == TaskStatus.COMPLETED) colorResource(id = R.color.gray)
        else colorResource(id = R.color.light)

        val painter = when (task.status){
            TaskStatus.INCOMPLETED -> painterResource(R.drawable.incompleted)
            TaskStatus.COMPLETED -> painterResource(R.drawable.completed)
            TaskStatus.SKIPPED -> painterResource(R.drawable.skipped)
        }
        Image(painter, null, modifier = Modifier.clickable { onToggleTask(task) })

        Text(
            text = task.title,
            color = textColor,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.weight(1f))

        // Task duration
        Text(
            text = "${task.durationMins} мин",
            color = textColor,
            fontSize = 14.sp
        )
    }
}

@Composable
fun BottomAction(
    task: Task?,
    accentColorPair: Pair<Color, Color>,
    onFinishClick: () -> Unit,
    onSkipClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(accentColorPair.first),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (task != null) {
            Text(
                text = task.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 28.dp),
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = accentColorPair.second
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)) {
                OutlinedButton(
                    onClick = onSkipClick,
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(2.dp, accentColorPair.second)
                ) {
                    Text(text = stringResource(id = R.string.skip), color = accentColorPair.second)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = onCompleteClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColorPair.second)
                ) {
                    Text(
                        text = stringResource(id = R.string.complete),
                        color = accentColorPair.first
                    )
                }
            }
        }
        else {
            Button(
                onClick = onFinishClick,
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = accentColorPair.second)
            ) {
                Text(
                    text = stringResource(id = R.string.finish),
                    color = accentColorPair.first
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TimeBar(startTime: Instant, totalTime: Int, accentColorPair: Pair<Color, Color>) {
    // Переменная для хранения прошедшего времени в секундах
    var secondsElapsed by remember { mutableStateOf(0) }

    // Обновляем `secondsElapsed` каждую секунду
    LaunchedEffect(startTime) {
        val startMillis = startTime.toEpochMilliseconds()
        while (true) {
            val nowMillis = Clock.System.now().toEpochMilliseconds()
            secondsElapsed = ((nowMillis - startMillis) / 1000).toInt()
            delay(1000L)
        }
    }

    // Вычисляем часы, минуты и секунды
    val hours = secondsElapsed / 3600
    val minutes = (secondsElapsed % 3600) / 60
    val seconds = secondsElapsed % 60

    val totalHours = totalTime / 60
    val totalMinutes = totalTime % 60

    // Форматируем строку времени
    val timeText = if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
    val totalTimeText = if (totalHours > 0) String.format("%d:%02d:00", totalHours, totalMinutes)
    else String.format("%d:00", totalMinutes)

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(accentColorPair.first),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ){
        Text(
            text = "$timeText / $totalTimeText",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = accentColorPair.second
        )
    }

}

@Preview
@Composable
fun RunningRoutineScreenPreview() {
    val viewModel = RunningRoutineViewModel(FakeRunningRoutineRepository(), Dispatchers.IO)
    RunningRoutineScreen(viewModel = viewModel)
}
