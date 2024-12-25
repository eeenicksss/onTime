package com.example.ontime.steps

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.ontime.create.presentation.CreateRoutineScreen
import com.example.ontime.create.presentation.CreateRoutineViewModel
import com.example.ontime.list.data.FakeRoutinesListRepository
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Rule

class CreateRoutineSteps {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Шаг Given - пользователь находится на экране создания рутины
    @Given("I am on the Create Routine screen")
    fun givenIAmOnCreateRoutineScreen() {
        composeTestRule.setContent {
            CreateRoutineScreen(
                viewModel = CreateRoutineViewModel(FakeRoutinesListRepository()),
                onSaveClick = {}
            )
        }
    }

    // Шаг When - пользователь вводит данные задачи
    @When("I enter {string} as task title and {string} as task duration")
    fun whenIEnterTaskData(title: String, duration: String) {
        composeTestRule.onNodeWithText("Task title")
            .performTextInput(title)

        composeTestRule.onNodeWithText("Task duration")
            .performTextInput(duration)

        composeTestRule.onNodeWithText("Add Task")
            .performClick()
    }

    // Шаг Then - проверяем, что задача добавлена в список
    @Then("the task {string} should appear in the task list")
    fun thenTaskShouldAppearInTheList(taskName: String) {
        composeTestRule.onNodeWithText(taskName)
            .assertIsDisplayed()
    }

    // Шаг Then - проверяем, что задача не добавлена
    @Then("the task should not be added")
    fun thenTaskShouldNotBeAdded() {
        composeTestRule.onNodeWithText("Task title")
            .assertExists()
        composeTestRule.onNodeWithText("Task duration")
            .assertExists()
    }

    // Шаг Then - проверяем, что появляется сообщение об ошибке
    @Then("I should see an error message saying {string}")
    fun thenIShouldSeeErrorMessage(errorMessage: String) {
        composeTestRule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }
}