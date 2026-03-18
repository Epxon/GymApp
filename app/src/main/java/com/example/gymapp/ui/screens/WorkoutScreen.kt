package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.gymapp.data.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    exercise: Exercise?,
    onSerieDone: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (exercise == null) return


    val sortedSeries = exercise.seriesList.sortedBy { it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(exercise.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(sortedSeries, key = { it.id }) { serie ->
                    SerieCard(
                        serieReps = serie.reps,
                        isCompleted = serie.isCompleted,
                        onDoneClick = { onSerieDone(serie.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SerieCard(
    serieReps: Int,
    isCompleted: Boolean,
    onDoneClick: () -> Unit
) {

    val textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
    val elevation = if (isCompleted) 1.dp else 4.dp

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = if (isCompleted) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) else CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$serieReps Reps",
                style = MaterialTheme.typography.titleLarge,
                textDecoration = textDecoration,
                fontWeight = if (isCompleted) FontWeight.Normal else FontWeight.Bold
            )

            if (!isCompleted) {
                Button(onClick = onDoneClick) {
                    Text("Listo")
                }
            }
        }
    }
}