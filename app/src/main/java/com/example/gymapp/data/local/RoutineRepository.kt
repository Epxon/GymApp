package com.example.gymapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gymapp.data.model.DayRoutine
import com.example.gymapp.data.model.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore(name = "gymapp_prefs")

class RoutineRepository(private val context: Context) {

    private val routinesKey = stringPreferencesKey("weekly_routines")

    fun getRoutines(): Flow<List<DayRoutine>> {
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[routinesKey]
            if (jsonString.isNullOrEmpty()) {
                defaultRoutines()
            } else {
                parseRoutines(jsonString)
            }
        }
    }

    suspend fun saveRoutines(routines: List<DayRoutine>) {
        context.dataStore.edit { preferences ->
            preferences[routinesKey] = routinesToJson(routines)
        }
    }

    private fun defaultRoutines(): List<DayRoutine> {
        return listOf(
            DayRoutine(
                dayName = "Lunes",
                workoutTitle = "Pecho",
                exercises = listOf(
                    Exercise("Press de banca", 4, 12),
                    Exercise("Aperturas", 3, 15)
                )
            ),
            DayRoutine(
                dayName = "Martes",
                workoutTitle = "Espalda",
                exercises = listOf(
                    Exercise("Dominadas", 3, 8),
                    Exercise("Remo", 4, 12),
                    Exercise("Curl", 3, 10)
                )
            ),
            DayRoutine(
                dayName = "Miércoles",
                workoutTitle = "Pierna",
                exercises = listOf(
                    Exercise("Sentadilla", 4, 10),
                    Exercise("Prensa", 4, 12)
                )
            ),
            DayRoutine(
                dayName = "Jueves",
                workoutTitle = "Hombro",
                exercises = listOf(
                    Exercise("Press militar", 4, 10),
                    Exercise("Elevaciones laterales", 3, 15)
                )
            ),
            DayRoutine(
                dayName = "Viernes",
                workoutTitle = "Bíceps",
                exercises = listOf(
                    Exercise("Curl con barra", 4, 12),
                    Exercise("Curl martillo", 3, 12)
                )
            ),
            DayRoutine(
                dayName = "Sábado",
                workoutTitle = "Tríceps",
                exercises = listOf(
                    Exercise("Fondos", 4, 10),
                    Exercise("Extensión en polea", 3, 15)
                )
            ),
            DayRoutine(
                dayName = "Domingo",
                workoutTitle = "Descanso",
                exercises = emptyList()
            )
        )
    }

    private fun routinesToJson(routines: List<DayRoutine>): String {
        val routinesArray = JSONArray()


        routines.forEach { routine ->
            val routineObject = JSONObject()
            routineObject.put("dayName", routine.dayName)
            routineObject.put("workoutTitle", routine.workoutTitle)

            val exercisesArray = JSONArray()
            routine.exercises.forEach { exercise ->
                val exerciseObject = JSONObject()
                exerciseObject.put("name", exercise.name)
                exerciseObject.put("sets", exercise.sets)
                exerciseObject.put("reps", exercise.reps)
                exercisesArray.put(exerciseObject)
            }

            routineObject.put("exercises", exercisesArray)
            routinesArray.put(routineObject)

        }

        return routinesArray.toString()
    }

    private fun parseRoutines(json: String): List<DayRoutine> {
        val routines = mutableListOf<DayRoutine>()
        val routinesArray = JSONArray(json)

        for (i in 0 until routinesArray.length()) {
            val routineObject = routinesArray.getJSONObject(i)
            val dayName = routineObject.getString("dayName")
            val workoutTitle = routineObject.getString("workoutTitle")

            val exercisesArray = routineObject.getJSONArray("exercises")
            val exercises = mutableListOf<Exercise>()

            for (j in 0 until exercisesArray.length()) {
                val exerciseObject = exercisesArray.getJSONObject(j)
                exercises.add(
                    Exercise(
                        name = exerciseObject.getString("name"),
                        sets = exerciseObject.getInt("sets"),
                        reps = exerciseObject.getInt("reps")
                    )
                )
            }

            routines.add(
                DayRoutine(
                    dayName = dayName,
                    workoutTitle = workoutTitle,
                    exercises = exercises
                )
            )
        }

        return routines
    }
}