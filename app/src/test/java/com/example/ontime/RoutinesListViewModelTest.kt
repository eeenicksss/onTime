package com.example.ontime

import com.example.ontime.list.domain.RoutinesListRepository
import com.example.ontime.list.presentation.Routine
import com.example.ontime.list.presentation.RoutinesListViewModel
import com.example.ontime.list.data.FakeRoutinesListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After

class RoutinesListViewModelTest {

    private lateinit var repository: RoutinesListRepository
    private lateinit var viewModel: RoutinesListViewModel
    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeRoutinesListRepository()
        viewModel = RoutinesListViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        // Reset the Main dispatcher after each test
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `initializes correctly and loads routines`() = runTest(testDispatcher) {
        val mockRoutines = listOf(
            Routine("1", "Routine 1", emptyList()),
            Routine("2", "Routine 2", emptyList())
        )
        repository.saveRoutine(Routine("1", "Routine 1", emptyList()))
        repository.saveRoutine(Routine("2", "Routine 2", emptyList()))
        viewModel.loadRoutines()

        advanceUntilIdle()
        val routines = viewModel.routines.first()
        assertEquals(mockRoutines, routines)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `deletes routine and updates list`() = runTest(testDispatcher) {
        repository.saveRoutine(Routine("1", "Routine 1", emptyList()))
        repository.saveRoutine(Routine("2", "Routine 2", emptyList()))
        repository.saveRoutine(Routine("3", "Routine 3", emptyList()))

        viewModel.loadRoutines()
        viewModel.deleteRoutine("2")
        advanceUntilIdle()
        val updatedRoutines = viewModel.routines.value
        val expectedRoutines = listOf(
            Routine("1", "Routine 1", emptyList()),
            Routine("3", "Routine 3", emptyList())
        )
        advanceUntilIdle()
        assertEquals(expectedRoutines, updatedRoutines)
    }
}