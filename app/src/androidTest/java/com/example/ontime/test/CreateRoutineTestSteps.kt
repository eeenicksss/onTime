package com.example.ontime.test

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.ontime.create.presentation.CreateRoutineScreen
import com.example.ontime.create.presentation.CreateRoutineViewModel
import com.example.ontime.list.data.FakeRoutinesListRepository
import com.example.ontime.navigation.AppNavHost
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Rule


class CreateRoutineTestSteps {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: CreateRoutineViewModel

//    @Before
//    fun setup() {
//        viewModel = CreateRoutineViewModel(FakeRoutinesListRepository())
//        composeTestRule.setContent {
//            CreateRoutineScreen(viewModel = viewModel, onSaveClick = {})
//        }
//    }

    @Given("I am on the \"Create Routine\" screen")
    fun i_am_on_create_routine_screen() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController)
            // Перейти на экран создания рутины
            navController.navigate("create_routine")
        }
    }

    @When("^I enter task name \"([^\"]*)\"$")
    fun iEnterTaskName(taskName: String) {
        composeTestRule.onNodeWithText("Task Title").performTextInput(taskName)
    }

    @When("^I enter task duration \"([^\"]*)\"$")
    fun iEnterTaskDuration(taskDuration: String) {
        composeTestRule.onNodeWithText("Task Duration").performTextInput(taskDuration)
    }

    @When("^I press \"Add Task\"$")
    fun iPressAddTask() {
        composeTestRule.onNodeWithText("Add Task").performClick()
    }

    @Then("^I should see \"([^\"]*)\" in the task list$")
    fun iShouldSeeTaskInList(expectedTask: String) {
        composeTestRule.onNodeWithText(expectedTask).assertExists()
    }

    @Then("^I should not see \"([^\"]*)\" in the task list$")
    fun iShouldNotSeeTaskInList(expectedTask: String) {
        composeTestRule.onNodeWithText(expectedTask).assertDoesNotExist()
    }

    @Then("^I should not see any task with duration \"([^\"]*)\" in the task list$")
    fun iShouldNotSeeTaskWithDuration(duration: String) {
        composeTestRule.onAllNodesWithText(duration).assertCountEquals(0)
    }
}
