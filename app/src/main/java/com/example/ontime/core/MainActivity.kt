package com.example.ontime.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.ontime.core.ui.theme.OnTimeTheme
import com.example.ontime.navigation.AppNavHost
import com.example.ontime.routine.presentation.RunningRoutineViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModel: RunningRoutineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnTimeTheme {
                setContent {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}