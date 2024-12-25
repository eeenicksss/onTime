package com.example.ontime

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.ontime.navigation.AppNavHost
import com.example.ontime.routine.domain.repository.FakeRunningRoutineRepository
import com.example.ontime.routine.presentation.RunningRoutineScreen
import com.example.ontime.routine.presentation.RunningRoutineViewModel
import kotlinx.coroutines.Dispatchers
import org.junit.Rule
import org.junit.Test

class OnTimeUITests {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun create_routine_button() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController) // встроить AppNavHost
        }

        composeTestRule.onNodeWithText("Новая рутина").assertIsDisplayed()
        composeTestRule.onNodeWithText("Новая рутина").performClick()
        composeTestRule.onNodeWithText("Сохранить рутину").assertIsDisplayed()
    }

    @Test
    fun create_routine() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController) // встроить AppNavHost
        }

        composeTestRule.onNodeWithText("Новая рутина").performClick()
        composeTestRule.onNodeWithText("Название рутины").performTextInput("CreateTest")
        composeTestRule.onNodeWithText("Заголовок задачи").performTextInput("Test")
        composeTestRule.onNodeWithText("Длительность задачи (мин)").performTextInput("1")
        composeTestRule.onNodeWithText("Добавить задачу").performClick()
        composeTestRule.onNodeWithText("Сохранить рутину").performClick()
        composeTestRule.onNodeWithText("CreateTest").assertIsDisplayed()
    }

    @Test
    fun start_routine() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController) // встроить AppNavHost
        }
        composeTestRule.onNodeWithText("Новая рутина").performClick()
        composeTestRule.onNodeWithText("Название рутины").performTextInput("Test")
        composeTestRule.onNodeWithText("Заголовок задачи").performTextInput("Test")
        composeTestRule.onNodeWithText("Длительность задачи (мин)").performTextInput("1")
        composeTestRule.onNodeWithText("Добавить задачу").performClick()
        composeTestRule.onNodeWithText("Сохранить рутину").performClick()

        composeTestRule.onNodeWithText("Новая рутина").assertIsDisplayed()
        composeTestRule.onNodeWithText("Начать").performClick()
        composeTestRule.onNodeWithText("Выполнить").assertIsDisplayed()
    }

    @Test
    fun delete_routine() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController) // встроить AppNavHost
        }
        composeTestRule.onNodeWithText("Новая рутина").performClick()
        composeTestRule.onNodeWithText("Название рутины").performTextInput("DeleteTest")
        composeTestRule.onNodeWithText("Заголовок задачи").performTextInput("Test")
        composeTestRule.onNodeWithText("Длительность задачи (мин)").performTextInput("1")
        composeTestRule.onNodeWithText("Добавить задачу").performClick()
        composeTestRule.onNodeWithText("Сохранить рутину").performClick()

        composeTestRule.onAllNodesWithText("Удалить").onLast().performClick()
        composeTestRule.onNodeWithText("DeleteTest").assertDoesNotExist()
    }

    @Test
    fun complete_task() {
        val viewModel = RunningRoutineViewModel(FakeRunningRoutineRepository(), Dispatchers.IO, "")

        composeTestRule.setContent {
            RunningRoutineScreen(viewModel = viewModel, onFinishClick = {})
        }

        composeTestRule.onNodeWithText("Выполнить").performClick()
        composeTestRule.onNodeWithContentDescription("COMPLETED").assertExists()
    }

    @Test
    fun skip_task() {
        val viewModel = RunningRoutineViewModel(FakeRunningRoutineRepository(), Dispatchers.IO, "")

        composeTestRule.setContent {
            RunningRoutineScreen(viewModel = viewModel, onFinishClick = {})
        }

        composeTestRule.onNodeWithText("Пропустить").performClick()
        composeTestRule.onNodeWithContentDescription("SKIPPED").assertExists()
    }
}