package com.example.gymapp.ui.screens

import androidx.compose.foundation.layout.*
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
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = timeText,
            fontSize = 72.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(100.dp))

        TextButton(onClick = onCancel) {
            Text("Saltar descanso")
        }
    }
}