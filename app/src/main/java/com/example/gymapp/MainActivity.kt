package com.example.gymapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.gymapp.app.GymAppViewModel
import com.example.gymapp.ui.screens.AddExerciseScreen
import com.example.gymapp.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {

    private val viewModel: GymAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            var showAddExerciseScreen by remember { mutableStateOf(false) }

            if (showAddExerciseScreen) {
                AddExerciseScreen(
                    routines = uiState.routines,
                    onSave = { dayName, workoutTitle, exercise ->
                        viewModel.addExerciseToDay(
                            dayName = dayName,
                            workoutTitle = workoutTitle,
                            exercise = exercise
                        )
                        showAddExerciseScreen = false
                    },
                    onCancel = {
                        showAddExerciseScreen = false
                    }
                )
            } else {
                HomeScreen(
                    routines = uiState.routines,
                    onAddExerciseClick = {
                        showAddExerciseScreen = true
                    }
                )
            }
        }
    }
}