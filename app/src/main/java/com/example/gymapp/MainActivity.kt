package com.example.gymapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapp.app.CurrentScreen
import com.example.gymapp.app.GymAppViewModel
import com.example.gymapp.ui.screens.AddExerciseScreen
import com.example.gymapp.ui.screens.HomeScreen
import com.example.gymapp.ui.screens.TimerScreen
import com.example.gymapp.ui.screens.WorkoutScreen
import com.example.gymapp.ui.themes.GymAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GymAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            GymAppTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                when (uiState.currentScreen) {
                    CurrentScreen.Home -> {
                        HomeScreen(
                            routines = uiState.routines,
                            onAddExerciseClick = { viewModel.navigateTo(CurrentScreen.AddExercise) },
                            onExerciseClick = { dayName, exercise ->
                                viewModel.startWorkoutSession(dayName, exercise)
                            }
                        )
                    }
                    CurrentScreen.AddExercise -> {
                        AddExerciseScreen(
                            routines = uiState.routines,
                            onSave = { dayName, workoutTitle, exercise ->
                                viewModel.addExerciseToDay(dayName, workoutTitle, exercise)
                            },
                            onCancel = { viewModel.navigateTo(CurrentScreen.Home) }
                        )
                    }
                    CurrentScreen.ActiveWorkout -> {
                        WorkoutScreen(
                            exercise = uiState.activeExercise,
                            onSerieDone = { serieId ->
                                viewModel.completeSerieAndStartTimer(serieId)
                            },
                            onBack = { viewModel.navigateTo(CurrentScreen.Home) }
                        )
                    }
                    CurrentScreen.Timer -> {
                        TimerScreen(
                            secondsRemaining = uiState.timerSecondsRemaining,
                            onAddMinute = { viewModel.addMinute() },
                            onSubtractMinute = { viewModel.subtractMinute() },
                            onCancel = { viewModel.cancelTimer() }
                        )
                    }
                }
            }
        }
    }
}