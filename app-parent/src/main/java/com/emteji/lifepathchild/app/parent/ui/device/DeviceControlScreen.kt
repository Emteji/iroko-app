package com.emteji.lifepathchild.app.parent.ui.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.emteji.lifepathchild.app.parent.navigation.ParentDestinations
import com.emteji.lifepathchild.app.parent.ui.components.IrokoButton
import com.emteji.lifepathchild.app.parent.ui.components.IrokoCard
import com.emteji.lifepathchild.app.parent.ui.components.IrokoDarkCard
import com.emteji.lifepathchild.app.parent.ui.components.ParentBottomNavigation
import com.emteji.lifepathchild.app.parent.ui.theme.*

@Composable
fun DeviceControlScreen(
    navController: NavController,
    viewModel: DeviceControlViewModel = hiltViewModel()
) {
    val selectedChild by viewModel.selectedChild.collectAsState()
    val children by viewModel.children.collectAsState()
    val policy by viewModel.policy.collectAsState()

    Scaffold(
        containerColor = IrokoCream,
        bottomBar = { 
            ParentBottomNavigation(
                navController = navController,
                currentRoute = ParentDestinations.DEVICE_CONTROL
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
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Device Control",
                    style = MaterialTheme.typography.headlineMedium,
                    color = IrokoBrown,
                    fontWeight = FontWeight.Bold
                )
            }

            // Child Selector
            item {
                if (children.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(children) { child ->
                            FilterChip(
                                selected = selectedChild?.id == child.id,
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
            }

            // Main Lock Toggle
            item {
                IrokoDarkCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (policy?.isLocked == true) "DEVICE LOCKED" else "DEVICE ACTIVE",
                                    color = if (policy?.isLocked == true) IrokoGold else IrokoWhite,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = if (policy?.isLocked == true) "Child cannot access apps" else "Standard restrictions apply",
                                    color = IrokoWhite.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Switch(
                                checked = policy?.isLocked == true,
                                onCheckedChange = { viewModel.toggleLock(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = IrokoBrown,
                                    checkedTrackColor = IrokoGold,
                                    uncheckedThumbColor = IrokoWhite,
                                    uncheckedTrackColor = Color.Gray
                                )
                            )
                        }
                    }
                }
            }

            // Screen Time Slider
            item {
                IrokoCard {
                    Column {
                        Text("Daily Screen Time Limit", fontWeight = FontWeight.Bold, color = IrokoBrown)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val limit = policy?.dailyLimitMinutes?.toFloat() ?: 120f
                        Text(
                            text = "${limit.toInt() / 60}h ${limit.toInt() % 60}m",
                            style = MaterialTheme.typography.displaySmall,
                            color = IrokoBrown
                        )
                        
                        Slider(
                            value = limit,
                            onValueChange = { viewModel.updateScreenTimeLimit(it.toInt()) },
                            valueRange = 0f..300f, // 0 to 5 hours
                            steps = 11, // 30 min increments
                            colors = SliderDefaults.colors(
                                thumbColor = IrokoBrown,
                                activeTrackColor = IrokoGold,
                                inactiveTrackColor = IrokoStone.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }
            
            // App Restrictions Mock
            item {
                IrokoCard {
                    Column {
                        Text("App Restrictions", fontWeight = FontWeight.Bold, color = IrokoBrown)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        AppRestrictionItem("YouTube", true)
                        Spacer(modifier = Modifier.height(12.dp))
                        AppRestrictionItem("Roblox", false)
                        Spacer(modifier = Modifier.height(12.dp))
                        AppRestrictionItem("Browser", true)
                    }
                }
            }

            // Emergency Unlock
            item {
                IrokoCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Emergency Unlock", fontWeight = FontWeight.Bold, color = IrokoBrown)
                            Text("Requires PIN", style = MaterialTheme.typography.bodySmall, color = IrokoStone)
                        }
                        Button(
                            onClick = { /* TODO: PIN Dialog */ },
                            colors = ButtonDefaults.buttonColors(containerColor = IrokoBrown.copy(alpha = 0.1f)),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Text("Enter PIN", color = IrokoBrown)
                        }
                    }
                }
            }
            
            // Preview
            item {
                Text("Lock Screen Preview", style = MaterialTheme.typography.titleMedium, color = IrokoBrown, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                ) {
                    // Simulating the child's lock screen
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.Lock, contentDescription = null, tint = IrokoGold, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Time to Focus", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Complete your tasks to unlock", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun AppRestrictionItem(appName: String, isAllowed: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Smartphone, contentDescription = null, tint = IrokoStone)
            Spacer(modifier = Modifier.width(16.dp))
            Text(appName, color = IrokoBrown)
        }
        Switch(
            checked = isAllowed,
            onCheckedChange = { },
            colors = SwitchDefaults.colors(
                checkedThumbColor = IrokoForestGreen,
                checkedTrackColor = IrokoForestGreen.copy(alpha = 0.3f)
            )
        )
    }
}
