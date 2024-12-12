package com.example.ontime


import com.example.ontime.create.presentation.CreateRoutineViewModel
import com.example.ontime.list.data.FakeRoutinesListRepository
import com.example.ontime.list.presentation.Routine
import com.example.ontime.routine.presentation.TaskStatus
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*


class CreateRoutineViewModelTest {
    private val repository = FakeRoutinesListRepository()
    private val viewModel = CreateRoutineViewModel(repository)

    @Test
    fun updateTaskTitle() {
        viewModel.updateTaskTitle("New Task")
        assertEquals("New Task", viewModel.newTaskTitle.value)
    }

    @Test
    fun updateTaskDuration() {
        viewModel.updateTaskDuration("30")
        assertEquals("30", viewModel.newTaskDuration.value)
    }

    @Test
    fun updateRoutineTitle() {
        viewModel.updateRoutineTitle("Morning Routine")
        assertEquals("Morning Routine", viewModel.routineTitle.value)
    }

    @Test
    fun addValidTask() {
        viewModel.updateTaskTitle("Task 1")
        viewModel.updateTaskDuration("60")
        viewModel.addTask()
        val tasks = viewModel.tasks.value
        assertEquals(1, tasks.size)
        assertEquals("Task 1", tasks[0].title)
        assertEquals(60, tasks[0].durationMins)
        assertEquals(TaskStatus.UNCOMPLETED, tasks[0].status)
    }

    @Test
    fun addInvalidTask() {
        viewModel.updateTaskTitle("Task 1")
        viewModel.updateTaskDuration("abc") // Invalid duration

        viewModel.addTask()

        val tasks = viewModel.tasks.value
        assertTrue(tasks.isEmpty())
    }

    @Test
    fun `saveRoutine adds a routine to the repository`() = runTest {
        val routine = Routine(id = "1", title = "Morning Routine", tasks = listOf())

        repository.saveRoutine(routine)

        val routines = repository.getAllRoutines()
        assertEquals(1, routines.size)
        assertEquals("Morning Routine", routines[0].title)
    }

    @Test
    fun `deleteRoutine removes a routine from the repository`() = runTest {
        val routine = Routine(id = "1", title = "Morning Routine", tasks = listOf())

        repository.saveRoutine(routine)
        repository.deleteRoutine("1")

        val routines = repository.getAllRoutines()
        assertTrue(routines.isEmpty())
    }

    @Test
    fun `getAllRoutines returns all saved routines`() = runTest {
        val routine1 = Routine(id = "1", title = "Morning Routine", tasks = listOf())
        val routine2 = Routine(id = "2", title = "Evening Routine", tasks = listOf())

        repository.saveRoutine(routine1)
        repository.saveRoutine(routine2)

        val routines = repository.getAllRoutines()
        assertEquals(2, routines.size)
        assertEquals("Morning Routine", routines[0].title)
        assertEquals("Evening Routine", routines[1].title)
    }
}