package com.example.gymapp.app

import com.example.gymapp.data.model.DayRoutine

data class GymAppState(
    val routines: List<DayRoutine> = emptyList()
)