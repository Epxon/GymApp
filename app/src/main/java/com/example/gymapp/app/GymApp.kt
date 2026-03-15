package com.example.gymapp.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.local.RoutineRepository
import com.example.gymapp.data.model.DayRoutine
import com.example.gymapp.data.model.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GymAppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RoutineRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(GymAppState())
    val uiState: StateFlow<GymAppState> = _uiState.asStateFlow()

    init {
        loadRoutines()
    }

    private fun loadRoutines() {
        viewModelScope.launch {
            repository.getRoutines().collect { routines ->
                _uiState.value = GymAppState(routines = routines)
            }
        }
    }

    fun saveRoutines(routines: List<DayRoutine>) {
        viewModelScope.launch {
            repository.saveRoutines(routines)
        }
    }

    fun addExerciseToDay(
        dayName: String,
        workoutTitle: String,
        exercise: Exercise
    ) {
        viewModelScope.launch {
            val currentRoutines = _uiState.value.routines

            val updatedRoutines = currentRoutines.map { routine ->
                if (routine.dayName == dayName) {
                    routine.copy(
                        workoutTitle = workoutTitle,
                        exercises = routine.exercises + exercise
                    )
                } else {
                    routine
                }
            }

            repository.saveRoutines(updatedRoutines)
        }
    }
}