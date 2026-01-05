package com.emteji.lifepathchild.app.parent.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.app.parent.ui.components.IrokoButton
import com.emteji.lifepathchild.app.parent.ui.components.IrokoCard
import com.emteji.lifepathchild.app.parent.ui.components.IrokoDarkCard
import com.emteji.lifepathchild.app.parent.ui.components.IrokoStatBar
import com.emteji.lifepathchild.app.parent.ui.theme.*
import androidx.navigation.NavController
import com.emteji.lifepathchild.app.parent.navigation.ParentDestinations
import com.emteji.lifepathchild.app.parent.ui.components.ParentBottomNavigation

@Composable
fun DashboardScreen(
    navController: NavController,
    onAddChildClick: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val children by viewModel.children.collectAsState()
    val selectedChild by viewModel.selectedChild.collectAsState()
    val recentMoods by viewModel.recentMoods.collectAsState()

    Scaffold(
        containerColor = IrokoCream,
        bottomBar = { 
            ParentBottomNavigation(
                navController = navController,
                currentRoute = ParentDestinations.DASHBOARD
            ) 
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Header (Greeting)
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Good Afternoon, Sarah",
                            style = MaterialTheme.typography.titleMedium,
                            color = IrokoBrown
                        )
                    }
                    IconButton(onClick = { /* TODO: Notifications */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = IrokoBrown
                        )
                    }
                }
            }

            // 2. Child Selector (Multi-Child Support)
            item {
                if (children.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(children) { child ->
                            val isSelected = selectedChild?.id == child.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectChild(child) },
                                label = { Text(child.name) },
                                leadingIcon = {
                                    if (isSelected) {
                                        Icon(
                                            Icons.Filled.Shield,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = IrokoBrown,
                                    selectedLabelColor = IrokoGold,
                                    selectedLeadingIconColor = IrokoGold,
                                    containerColor = IrokoWhite,
                                    labelColor = IrokoStone
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = Color.Transparent,
                                    selectedBorderColor = Color.Transparent,
                                    enabled = true,
                                    selected = isSelected
                                )
                            )
                        }
                        
                        // Add Child Button
                        item {
                            FilterChip(
                                selected = false,
                                onClick = onAddChildClick,
                                label = { Text("Add Child") },
                                leadingIcon = { Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = IrokoWhite,
                                    labelColor = IrokoBrown,
                                    iconColor = IrokoBrown
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = IrokoBrown.copy(alpha = 0.3f),
                                    enabled = true,
                                    selected = false
                                )
                            )
                        }
                    }
                }
            }

            // 3. Main Stats Card (Dynamic based on Selected Child)
            item {
                if (selectedChild != null) {
                    IrokoCard {
                        // Child Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar Placeholder
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(IrokoBrown),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = selectedChild!!.name.take(1).uppercase(),
                                    color = IrokoGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = selectedChild!!.name,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = IrokoBrown,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Filled.Shield, // Verified Icon
                                        contentDescription = "Verified",
                                        tint = IrokoGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = "Level 5 • ${selectedChild!!.gritScore}/700 XP",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = IrokoStone
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val moodEmoji = when(recentMoods.firstOrNull()?.moodLabel) {
                                "Happy" -> "😊"
                                "Sad" -> "😔"
                                "Angry" -> "😠"
                                "Tired" -> "😴"
                                "Proud" -> "😎"
                                "Calm" -> "😌"
                                else -> "😐"
                            }
                            StatItem("Recent Mood", moodEmoji, IrokoGold)
                            StatItem("Grit", "A-", IrokoForestGreen)
                            StatItem("Discipline", "B+", IrokoBrown)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Restriction Status Banner
                        IrokoDarkCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Lock, contentDescription = null, tint = IrokoGold)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Device Restricted",
                                        color = IrokoWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "1 Task Remains",
                                    color = IrokoGold,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                } else {
                    // Empty State
                    IrokoCard {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No children added yet.", color = IrokoStone)
                            Spacer(modifier = Modifier.height(16.dp))
                            IrokoButton(text = "Add Child", onClick = onAddChildClick)
                        }
                    }
                }
            }

            // 4. Tasks Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tasks for ${selectedChild?.name ?: "Child"}",
                        style = MaterialTheme.typography.titleLarge,
                        color = IrokoBrown,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = { /* See All */ }) {
                        Text("See All", color = IrokoStone)
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Task List (Mock Data)
                TaskItem(
                    title = "Clear the Living Room",
                    deadline = "Due by Dusk",
                    points = 15,
                    isCritical = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                TaskItem(
                    title = "Practice Multiplication",
                    deadline = "Due Today",
                    points = 10,
                    isCritical = false
                )
                Spacer(modifier = Modifier.height(12.dp))
                TaskItem(
                    title = "Feed the Chickens",
                    deadline = "Due by Dusk",
                    points = 15,
                    isCritical = false
                )
            }

            // 5. Community Teaser
            item {
                Spacer(modifier = Modifier.height(8.dp))
                IrokoCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                             Icon(Icons.Filled.People, contentDescription = null, tint = IrokoBrown)
                             Spacer(modifier = Modifier.width(12.dp))
                             Column {
                                 Text("Community Benchmark", fontWeight = FontWeight.Bold, color = IrokoBrown)
                                 Text("Top 23% this week", style = MaterialTheme.typography.bodySmall, color = IrokoForestGreen)
                             }
                        }
                        Text("#453", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = IrokoBrown)
                    }
                }
                Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for nav bar
            }
        }
    }
}

@Composable
fun StatItem(label: String, grade: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = IrokoStone)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = grade,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun TaskItem(title: String, deadline: String, points: Int, isCritical: Boolean) {
    IrokoCard(elevation = 1.dp) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isCritical) Icons.Filled.Shield else Icons.Filled.Add, // Placeholder icons
                    contentDescription = null,
                    tint = if (isCritical) IrokoBurntGold else IrokoBrown,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, color = IrokoBrown)
                    Text(text = deadline, style = MaterialTheme.typography.bodySmall, color = IrokoStone)
                }
            }
            
            Surface(
                color = IrokoCream,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, IrokoGold.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Shield, contentDescription = null, tint = IrokoGold, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "$points", fontWeight = FontWeight.Bold, color = IrokoBrown, fontSize = 12.sp)
                }
            }
        }
    }
}
