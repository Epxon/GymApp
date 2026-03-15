package com.example.gymapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.gymapp.data.model.DayRoutine
import com.example.gymapp.data.model.Exercise


@Composable
fun AddExerciseScreen(
    routines: List<DayRoutine>,
    onSave: (dayName: String, workoutTitle: String, exercise: Exercise) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (routines.isEmpty()) return

    var selectedDayIndex by remember { mutableIntStateOf(0) }
    var workoutTitle by remember { mutableStateOf(routines[selectedDayIndex].workoutTitle) }
    var exerciseName by remember { mutableStateOf("") }
    var setsText by remember { mutableStateOf("") }
    var repsText by remember { mutableStateOf("") }

    val isFormValid = exerciseName.isNotBlank() &&
            workoutTitle.isNotBlank() &&
            setsText.toIntOrNull() != null &&
            repsText.toIntOrNull() != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Agregar ejercicio",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Selecciona el día",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(routines.indices.toList()) { index ->
                val isSelected = index == selectedDayIndex

                Surface(
                    modifier = Modifier.clickable {
                        selectedDayIndex = index
                        workoutTitle = routines[index].workoutTitle
                    },
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = if (isSelected) 6.dp else 1.dp
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = routines[index].dayName)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = workoutTitle,
                    onValueChange = { workoutTitle = it },
                    label = { Text("Título del día") },
                    placeholder = { Text("Ej. Espalda") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = exerciseName,
                    onValueChange = { exerciseName = it },
                    label = { Text("Nombre del ejercicio") },
                    placeholder = { Text("Ej. Dominadas") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = setsText,
                    onValueChange = { setsText = it.filter { char -> char.isDigit() } },
                    label = { Text("Series") },
                    placeholder = { Text("Ej. 4") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = repsText,
                    onValueChange = { repsText = it.filter { char -> char.isDigit() } },
                    label = { Text("Repeticiones") },
                    placeholder = { Text("Ej. 12") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancelar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    val sets = setsText.toIntOrNull() ?: return@Button
                    val reps = repsText.toIntOrNull() ?: return@Button

                    onSave(
                        routines[selectedDayIndex].dayName,
                        workoutTitle,
                        Exercise(
                            name = exerciseName.trim(),
                            sets = sets,
                            reps = reps
                        )
                    )
                },
                enabled = isFormValid
            ) {
                Text("Guardar")
            }
        }
    }
}