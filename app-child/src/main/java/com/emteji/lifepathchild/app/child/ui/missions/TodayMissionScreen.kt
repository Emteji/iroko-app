package com.emteji.lifepathchild.app.child.ui.missions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.core.ui.components.IrokoBackground

@Composable
fun TodayMissionScreen(
    onStartMission: (String) -> Unit, // Task ID
    viewModel: TodayMissionViewModel = hiltViewModel()
) {
    val tasks by viewModel.dailyTasks.collectAsState()

    IrokoBackground(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Your Day",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // MORNING
                item { SectionHeader("Morning") }
                items(tasks.filter { it.timeOfDay == TimeOfDay.MORNING }) { task ->
                    TaskRow(task = task, onClick = { onStartMission(task.id) })
                }

                // AFTERNOON
                item { SectionHeader("Afternoon") }
                items(tasks.filter { it.timeOfDay == TimeOfDay.AFTERNOON }) { task ->
                    TaskRow(task = task, onClick = { onStartMission(task.id) })
                }

                // EVENING
                item { SectionHeader("Evening") }
                items(tasks.filter { it.timeOfDay == TimeOfDay.EVENING }) { task ->
                    TaskRow(task = task, onClick = { onStartMission(task.id) })
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun TaskRow(task: DailyTaskUiModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Icon (Placeholder)
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Star, // Replace with specific icon based on task type
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = task.instruction,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Status Dot
        val dotColor = when (task.status) {
            TaskStatus.PENDING -> Color.Gray.copy(alpha = 0.5f)
            TaskStatus.COMPLETED -> MaterialTheme.colorScheme.tertiary // Soft Green
            TaskStatus.MISSED -> Color.Transparent
        }
        
        val dotModifier = Modifier
            .size(12.dp)
            .clip(CircleShape)

        if (task.status == TaskStatus.MISSED) {
            Box(
                modifier = dotModifier.border(2.dp, MaterialTheme.colorScheme.error, CircleShape) // Amber/Error outline
            )
        } else {
            Box(
                modifier = dotModifier.background(dotColor)
            )
        }
    }
}

