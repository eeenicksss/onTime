package com.example.ontime

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.example.ontime.routine.domain.repository.RunningRoutineRepository
import com.example.ontime.routine.presentation.RunningRoutineViewModel
import com.example.ontime.routine.presentation.Task
import com.example.ontime.routine.presentation.TaskStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RunningRoutineViewModelTest {

    private lateinit var repository: TestRunningRoutineRepository
    private lateinit var viewModel: RunningRoutineViewModel
    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @Before
    fun setup() {
        repository = TestRunningRoutineRepository()
        viewModel = RunningRoutineViewModel(repository, testDispatcher, "routineId", false)
    }

    @Test
    fun `initializes correctly and loads tasks`() = runTest(testDispatcher) {
        repository.setRoutineTasks(listOf(
            Task("Task 1", 10, TaskStatus.UNCOMPLETED),
            Task("Task 2", 15, TaskStatus.COMPLETED)
        ))

        val emittedValues: MutableList<List<Task>> = mutableListOf()
        val job = launch {
            viewModel.tasks.collect { emittedValues += listOf(it) }
        }

        advanceUntilIdle()

        assertEquals(2, emittedValues.last().size)
        job.cancel()
    }

    @Test
    fun `updates accent color based on time`() = runTest(testDispatcher) {
        repository.setRoutineTasks(listOf(
            Task("Task 1", 10, TaskStatus.UNCOMPLETED)
        ))

        viewModel.updateStartTime(LocalDateTime(2023, 12, 12, 10, 0))
        viewModel.setSecondsElapsed(0) // 10 minutes elapsed
        assertEquals(R.color.gray to R.color.light, viewModel.accentColorIdPair.value)

        viewModel.setSecondsElapsed(1200) // Exceeds task duration
        assertEquals(R.color.red to R.color.light, viewModel.accentColorIdPair.value)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun `timePickerStateToLocalDateTime converts time correctly`() = runTest(testDispatcher) {
        val mockTimePickerState = TimePickerState(14, 30, true)

        val result = viewModel.timePickerStateToLocalDateTime(mockTimePickerState)
        val expected = LocalDateTime(2023, 12, 12, 14, 30)

        assertEquals(expected.hour, result.hour)
        assertEquals(expected.minute, result.minute)
    }
}

class TestRunningRoutineRepository : RunningRoutineRepository {
    private var tasks: List<Task> = emptyList()

    override suspend fun getRoutineById(routineId: String): List<Task> {
        return tasks
    }

    override suspend fun saveTasks(tasks: List<Task>) {
        this.tasks = tasks
    }

    fun setRoutineTasks(tasks: List<Task>) {
        this.tasks = tasks
    }
}