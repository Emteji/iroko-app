package com.emteji.lifepathchild.app.child.ui.scenarios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.core.data.model.Scenario
import com.emteji.lifepathchild.core.ui.components.AppCard

@Composable
fun ScenariosScreen(
    viewModel: ChildScenariosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Roleplay Scenarios", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            } else if (uiState.scenarios.isEmpty()) {
                Text("No active scenarios available.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(uiState.scenarios) { scenario ->
                        ScenarioItem(
                            scenario = scenario,
                            onChoiceSelected = { choice -> 
                                viewModel.submitChoice(scenario.id, choice.text) 
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ScenarioItem(
    scenario: Scenario,
    onChoiceSelected: (com.emteji.lifepathchild.core.data.model.ScenarioChoice) -> Unit
) {
    AppCard(
        title = "What would you do?",
        description = scenario.description
    ) {
        Column(modifier = Modifier.padding(top = 8.dp)) {
            scenario.choices.forEach { choice ->
                Button(
                    onClick = { onChoiceSelected(choice) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(choice.text)
                }
            }
        }
    }
}
