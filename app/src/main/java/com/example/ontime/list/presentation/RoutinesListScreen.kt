package com.example.ontime.list.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.ontime.R
import com.example.ontime.create.presentation.CreateRoutineActivity
import com.example.ontime.di.AppComponent
import com.example.ontime.routine.presentation.RunningRoutineActivity

class RoutinesListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("RoutinesListActivity", "onCreate invoked")
        val repository = AppComponent.instance.provideRoutinesListRepository()
        val viewModel = ViewModelProvider(
            this,
            RoutinesListViewModelFactory(repository)
        )[RoutinesListViewModel::class.java]

        setContent {
            RoutinesListScreen(
                viewModel = viewModel,
                onCreateRoutineClick = {
                    startActivity(Intent(this, CreateRoutineActivity::class.java))
                },
                onStartRoutineClick = { routineId ->
                    Log.d("RoutinesListActivity", "Starting routine with ID: $routineId")
                    startActivity(
                        Intent(this, RunningRoutineActivity::class.java)
                            .putExtra("routine_id", routineId)
                    )
                }
            )
        }
    }
}

@Composable
fun RoutinesListScreen(
    viewModel: RoutinesListViewModel,
    onCreateRoutineClick: () -> Unit,
    onStartRoutineClick: (String) -> Unit
) {
    val routines by viewModel.routines.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .systemBarsPadding()
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(routines) { routine ->
                RoutineItem(
                    routine = routine,
                    onDeleteClick = { viewModel.deleteRoutine(routine.id) },
                    onStartClick = {
                        Log.d("RoutinesListActivity", "onStartClick invoked for routine ID: ${routine.id}")
                        onStartRoutineClick(routine.id)
                    }
                )
            }
        }
        Button(
            onClick = onCreateRoutineClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.create_routine))
        }
    }
}

@Composable
fun RoutineItem(
    routine: Routine,
    onDeleteClick: () -> Unit,
    onStartClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = routine.title, fontWeight = FontWeight.Bold)
            Text(text = "${routine.tasks.size} tasks")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onStartClick) {
                Text(text = stringResource(id = R.string.start))
            }
            OutlinedButton(onClick = onDeleteClick) {
                Text(text = stringResource(id = R.string.delete))
            }
        }
    }
}
