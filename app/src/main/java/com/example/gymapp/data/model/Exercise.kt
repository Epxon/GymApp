package com.example.gymapp.data.model

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: Int,
    val seriesList: List<ExerciseSerie> = (1..sets).map { id ->
        ExerciseSerie(id = id, reps = reps)
    }
)