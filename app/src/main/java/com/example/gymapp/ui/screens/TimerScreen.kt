package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun TimerScreen(
    secondsRemaining: Long,
    onAddMinute: () -> Unit,      // NUEVO
    onSubtractMinute: () -> Unit, // NUEVO
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = secondsRemaining / 60
    val secs = secondsRemaining % 60
    val timeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Descanso",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Fila con controles de tiempo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Botón Menos
            FilledIconButton(
                onClick = onSubtractMinute,
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Quitar minuto")
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Tiempo Central
            Text(
                text = timeText,
                fontSize = 80.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(24.dp))

            // Botón Más
            FilledIconButton(
                onClick = onAddMinute,
                modifier = Modifier.size(56.dp),
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir minuto")
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = onCancel,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Saltar descanso")
        }
    }
}