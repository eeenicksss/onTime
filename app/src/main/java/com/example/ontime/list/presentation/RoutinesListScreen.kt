package com.example.ontime.list.presentation

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ontime.R

@Composable
fun RoutinesListScreen(
    viewModel: RoutinesListViewModel,
    onCreateRoutineClick: () -> Unit,
    onStartRoutineClick: (String) -> Unit
) {
    val routines by viewModel.routines.collectAsState()

    Column(
        modifier = Modifier
            .background(colorResource(R.color.background))
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
                        onStartRoutineClick(routine.id)
                    }
                )
            }
        }
        Button(
            onClick = onCreateRoutineClick,
            modifier = Modifier.height(52.dp).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.green),
                contentColor = colorResource(R.color.on_green)
            ),
            shape = RoundedCornerShape(20.dp)
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
            Text(
                text = routine.title,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.light)
            )
            Text(
                text = "${routine.tasks.size} tasks",
                color = colorResource(R.color.gray)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.green),
                    contentColor = colorResource(R.color.on_green)
                )) {
                Text(
                    text = stringResource(id = R.string.start),
                    color = colorResource(R.color.on_green)
                )
            }
            OutlinedButton(
                onClick = onDeleteClick,
                border = BorderStroke(2.dp, colorResource(R.color.red))
            ) {
                Text(
                    text = stringResource(id = R.string.delete),
                    color = colorResource(R.color.red)
                )
            }
        }
    }
}
