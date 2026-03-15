package com.example.gymapp.data.model

data class DayRoutine(
    val dayName: String,
    val workoutTitle: String,
    val exercises: List<Exercise>
)