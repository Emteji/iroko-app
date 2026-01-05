package com.emteji.lifepathchild.app.parent.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.app.parent.ui.components.IrokoButton
import com.emteji.lifepathchild.app.parent.ui.components.IrokoCard
import com.emteji.lifepathchild.app.parent.ui.components.IrokoDarkCard
import androidx.navigation.NavController
import com.emteji.lifepathchild.app.parent.navigation.ParentDestinations
import com.emteji.lifepathchild.app.parent.ui.components.ParentBottomNavigation
import com.emteji.lifepathchild.app.parent.ui.theme.*
import com.emteji.lifepathchild.core.data.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskGovernanceScreen(
    navController: NavController,
    viewModel: TaskGovernanceViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    val children by viewModel.children.collectAsState()
    val selectedChild by viewModel.selectedChild.collectAsState()
    
    var showCreateSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = IrokoCream,
        bottomBar = { 
            ParentBottomNavigation(
                navController = navController,
                currentRoute = ParentDestinations.TASKS
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateSheet = true },
                containerColor = IrokoBrown,
                contentColor = IrokoGold
            ) {
                Icon(Icons.Filled.Add, "Create Mission")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mission Control",
                    style = MaterialTheme.typography.headlineMedium,
                    color = IrokoBrown,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Child Selector (Compact)
            if (children.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(children) { child ->
                        val isSelected = selectedChild?.id == child.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectChild(child) },
                            label = { Text(child.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IrokoBrown,
                                selectedLabelColor = IrokoGold,
                                containerColor = IrokoWhite
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Task List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tasks) { task ->
                    TaskGovernanceItem(task)
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Fab padding
                }
            }
        }
        
        if (showCreateSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCreateSheet = false },
                containerColor = IrokoCream
            ) {
                CreateTaskSheetContent(
                    onDismiss = { showCreateSheet = false },
                    onCreate = { title, points, diff, isUnlock, deadline ->
                        viewModel.createTask(title, points, diff, isUnlock, deadline) {
                            showCreateSheet = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TaskGovernanceItem(task: Task) {
    IrokoCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (task.isUnlockCondition) Icons.Filled.Lock else Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (task.isUnlockCondition) IrokoBrown else IrokoGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = IrokoBrown
                        )
                        Text(
                            text = "${task.difficulty} • ${task.dueDate ?: "No Deadline"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = IrokoStone
                        )
                    }
                }
                
                Text(
                    text = "${task.rewardPoints} XP",
                    fontWeight = FontWeight.Bold,
                    color = IrokoForestGreen
                )
            }
        }
    }
}

@Composable
fun CreateTaskSheetContent(
    onDismiss: () -> Unit,
    onCreate: (String, Int, String, Boolean, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var points by remember { mutableStateOf("10") }
    var difficulty by remember { mutableStateOf("Medium") }
    var isUnlock by remember { mutableStateOf(false) }
    var deadline by remember { mutableStateOf("Due by Dusk") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "New Mission",
            style = MaterialTheme.typography.headlineSmall,
            color = IrokoBrown,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Mission Title") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IrokoBrown,
                focusedLabelColor = IrokoBrown
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = points,
                onValueChange = { points = it.filter { char -> char.isDigit() } },
                label = { Text("XP Reward") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                 colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IrokoBrown,
                    focusedLabelColor = IrokoBrown
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Difficulty Dropdown (Simplified as text for MVP)
            OutlinedTextField(
                value = difficulty,
                onValueChange = { difficulty = it },
                label = { Text("Difficulty") },
                modifier = Modifier.weight(1f),
                 colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IrokoBrown,
                    focusedLabelColor = IrokoBrown
                )
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Unlock Toggle
        IrokoDarkCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isUnlock = !isUnlock }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Device Unlock Key", color = IrokoGold, fontWeight = FontWeight.Bold)
                    Text(
                        "Must complete to use apps",
                        color = IrokoWhite.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(
                    checked = isUnlock,
                    onCheckedChange = { isUnlock = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = IrokoBrown,
                        checkedTrackColor = IrokoGold
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        IrokoButton(
            text = "Assign Mission",
            onClick = {
                if (title.isNotEmpty()) {
                    onCreate(title, points.toIntOrNull() ?: 10, difficulty, isUnlock, deadline)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}
