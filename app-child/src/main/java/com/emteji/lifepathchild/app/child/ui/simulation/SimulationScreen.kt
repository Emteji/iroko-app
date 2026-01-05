package com.emteji.lifepathchild.app.child.ui.simulation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SimulationScreen(
    viewModel: SimulationViewModel = hiltViewModel()
) {
    val scenario by viewModel.currentScenario.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    val outcome by viewModel.outcome.collectAsState()

    if (scenario == null) return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF263238)) // Dark cinematic background
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (outcome == null) {
            // Story Text
            Text(
                text = scenario!!.storyText,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Timer
            LinearProgressIndicator(
                progress = { timeRemaining / scenario!!.timeLimitSeconds.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(bottom = 32.dp),
                color = if (timeRemaining < 3) Color.Red else Color.Cyan,
            )

            // Options
            scenario!!.options.forEach { option ->
                Button(
                    onClick = { viewModel.onOptionSelected(option) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF37474F))
                ) {
                    Text(text = option.text, fontSize = 18.sp, color = Color.White)
                }
            }
        } else {
            // Outcome / Feedback
            Text(
                text = "Simulation Complete",
                color = Color.Gray,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = outcome!!,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
