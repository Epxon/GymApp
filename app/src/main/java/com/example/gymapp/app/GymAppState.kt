package com.example.gymapp.app

import com.example.gymapp.data.model.DayRoutine
import com.example.gymapp.data.model.Exercise

sealed class CurrentScreen {
    object Home : CurrentScreen()
    object AddExercise : CurrentScreen()
    object ActiveWorkout : CurrentScreen()
    object Timer : CurrentScreen()
}

data class GymAppState(
    val routines: List<DayRoutine> = emptyList(),
    val currentScreen: CurrentScreen = CurrentScreen.Home,
    val selectedDayName: String? = null,
    val activeExercise: Exercise? = null,
    val timerSecondsRemaining: Long = 0,
    val isTimerRunning: Boolean = false
)