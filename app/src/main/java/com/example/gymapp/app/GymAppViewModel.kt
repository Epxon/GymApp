package com.example.gymapp.app

import android.Manifest
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.local.RoutineRepository
import com.example.gymapp.data.model.Exercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GymAppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RoutineRepository(application.applicationContext)

    private val _uiState = MutableStateFlow(GymAppState())
    val uiState: StateFlow<GymAppState> = _uiState.asStateFlow()

    private var countDownTimer: CountDownTimer? = null
    private val DEFAULT_TIMER_MS = 150_000L

    init {
        loadRoutines()
    }

    private fun loadRoutines() {
        viewModelScope.launch {
            repository.getRoutines().collect { routines ->
                _uiState.update { it.copy(routines = routines) }
            }
        }
    }

    // --- NAVEGACIÓN ---

    fun navigateTo(screen: CurrentScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun startWorkoutSession(dayName: String, exercise: Exercise) {
        _uiState.update {
            it.copy(
                currentScreen = CurrentScreen.ActiveWorkout,
                selectedDayName = dayName,
                activeExercise = exercise
            )
        }
    }

    // --- LÓGICA DE EJERCICIOS ---

    fun addExerciseToDay(dayName: String, workoutTitle: String, exercise: Exercise) {
        viewModelScope.launch {
            val updatedRoutines = _uiState.value.routines.map { routine ->
                if (routine.dayName == dayName) {
                    routine.copy(workoutTitle = workoutTitle, exercises = routine.exercises + exercise)
                } else routine
            }
            repository.saveRoutines(updatedRoutines)
            navigateTo(CurrentScreen.Home) // Volver al inicio tras guardar
        }
    }

    fun completeSerieAndStartTimer(serieId: Int) {
        val currentDay = _uiState.value.selectedDayName ?: return
        val currentExercise = _uiState.value.activeExercise ?: return

        // 1. Actualizar el ejercicio activo en memoria
        val updatedSeries = currentExercise.seriesList.map {
            if (it.id == serieId) it.copy(isCompleted = true) else it
        }
        val updatedExercise = currentExercise.copy(seriesList = updatedSeries)

        // 2. Actualizar el repositorio (DB)
        viewModelScope.launch {
            val updatedRoutines = _uiState.value.routines.map { routine ->
                if (routine.dayName == currentDay) {
                    val updatedExercisesList = routine.exercises.map {
                        if (it.name == updatedExercise.name) updatedExercise else it
                    }
                    routine.copy(exercises = updatedExercisesList)
                } else routine
            }
            repository.saveRoutines(updatedRoutines)
        }

        // 3. Actualizar estado local y arrancar temporizador
        _uiState.update { it.copy(activeExercise = updatedExercise) }
        startTimer()
    }

    // --- LÓGICA DEL TEMPORIZADOR ---

    private fun startTimer() {
        countDownTimer?.cancel() // Cancelar si ya había uno

        _uiState.update { it.copy(
            currentScreen = CurrentScreen.Timer,
            timerSecondsRemaining = DEFAULT_TIMER_MS / 1000,
            isTimerRunning = true
        ) }

        countDownTimer = object : CountDownTimer(DEFAULT_TIMER_MS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _uiState.update { it.copy(timerSecondsRemaining = millisUntilFinished / 1000) }
            }

            @RequiresPermission(Manifest.permission.VIBRATE)
            override fun onFinish() {
                _uiState.update { it.copy(isTimerRunning = false) }
                vibrate()
                // Volver automáticamente a la pantalla de series
                navigateTo(CurrentScreen.ActiveWorkout)
            }
        }.start()
    }

    fun cancelTimer() {
        countDownTimer?.cancel()
        _uiState.update { it.copy(isTimerRunning = false) }
        navigateTo(CurrentScreen.ActiveWorkout)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun vibrate() {
        val vibrator = getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }
    }
}