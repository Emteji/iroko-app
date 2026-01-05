package com.emteji.lifepathchild.app.child.ui.dailypath

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.core.data.model.Task

@Composable
fun DailyPathScreen(
    onTaskClick: (String) -> Unit,
    viewModel: DailyPathViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    val childName by viewModel.childName.collectAsState()

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        bottomBar = { LockedAppsBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            // Companion Header
            Text(
                text = "Good morning, $childName.",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF52361E), // Iroko Brown
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "I have prepared your path for today. Completing these will unlock your device time.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF757575)
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // Task List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(task = task, onClick = { onTaskClick(task.id) })
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (task.isUnlockCondition) Color(0xFFFFF8E1) else Color(0xFFE8F5E9), 
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = task.title.take(1),
                    fontWeight = FontWeight.Bold,
                    color = if (task.isUnlockCondition) Color(0xFFFF8F00) else Color(0xFF2E7D32)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212121)
                )
                if (task.difficulty != "Medium") {
                    Text(
                        text = "${task.difficulty} Difficulty",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF757575)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))

            // Points Badge
            Surface(
                color = Color(0xFFFFF3E0),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Star, 
                        contentDescription = null, 
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+${task.rewardPoints}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF6C00),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LockedAppsBar() {
    Surface(
        color = Color(0xFF263238), // Dark Slate
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Entertainment apps are currently locked.",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
