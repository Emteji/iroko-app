package com.emteji.lifepathchild.app.child.ui.missions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.core.data.model.Mission
import com.emteji.lifepathchild.core.data.model.MissionStatus
import com.emteji.lifepathchild.core.ui.components.AppCard

@Composable
fun DailyMissionsScreen(
    viewModel: ChildMissionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "My Missions", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.missions) { mission ->
                        MissionItem(
                            mission = mission,
                            onComplete = { viewModel.completeMission(mission.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MissionItem(
    mission: Mission,
    onComplete: () -> Unit
) {
    AppCard(
        title = mission.description,
        description = "Type: ${mission.type.name}\nStatus: ${mission.status.name}"
    ) {
        if (mission.status == MissionStatus.ASSIGNED) {
            Button(onClick = onComplete) {
                Text("Mark Complete")
            }
        } else {
             Text(
                 text = if (mission.status == MissionStatus.VERIFIED) "Verified! +XP" else "Pending Verification",
                 color = MaterialTheme.colorScheme.primary
             )
        }
    }
}
