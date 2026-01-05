package com.emteji.lifepathchild.app.parent.ui.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.app.parent.ui.theme.IrokoBrown
import com.emteji.lifepathchild.app.parent.ui.theme.IrokoCream
import com.emteji.lifepathchild.app.parent.ui.theme.IrokoGold

@Composable
fun VillageSetupScreen(
    onSetupComplete: () -> Unit,
    viewModel: VillageSetupViewModel = hiltViewModel()
) {
    var step by remember { mutableStateOf(0) }
    
    // Step 1: Child Basic Info
    var childName by remember { mutableStateOf("") }
    var childAge by remember { mutableStateOf("") }
    
    // Step 2: Values
    var selectedValues by remember { mutableStateOf(setOf<String>()) }
    
    // Step 3: Environment
    var environment by remember { mutableStateOf("Urban") }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IrokoCream)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "Village Context",
                style = MaterialTheme.typography.headlineMedium,
                color = IrokoBrown,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = when(step) {
                    0 -> "Who are we raising?"
                    1 -> "What values matter most?"
                    2 -> "Where are they growing?"
                    else -> ""
                },
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            when(step) {
                0 -> {
                    OutlinedTextField(
                        value = childName,
                        onValueChange = { childName = it },
                        label = { Text("Child's First Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = childAge,
                        onValueChange = { childAge = it },
                        label = { Text("Age") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                1 -> {
                    // Values Selection
                    val values = listOf("Respect Elders", "Hard Work", "Academic Excellence", "Creativity", "Faith/Spirituality", "Cleanliness")
                    values.forEach { value ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Checkbox(
                                checked = selectedValues.contains(value),
                                onCheckedChange = {
                                    selectedValues = if (it) selectedValues + value else selectedValues - value
                                },
                                colors = CheckboxDefaults.colors(checkedColor = IrokoGold)
                            )
                            Text(text = value, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
                2 -> {
                    // Environment Selection
                    val envs = listOf("Urban (City)", "Rural (Town)", "Diaspora (Abroad)")
                    envs.forEach { env ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = environment == env.split(" ")[0],
                                onClick = { environment = env.split(" ")[0] },
                                colors = RadioButtonDefaults.colors(selectedColor = IrokoGold)
                            )
                            Text(text = env, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }

            if (error != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    if (step < 2) {
                        step++
                    } else {
                        // Submit
                        viewModel.createVillageContext(
                            name = childName, 
                            age = childAge, 
                            values = selectedValues, 
                            environment = environment,
                            onSuccess = onSetupComplete
                        )
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IrokoBrown)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = IrokoGold, modifier = Modifier.size(24.dp))
                } else {
                    Text(if (step < 2) "Next" else "Create Village", color = IrokoGold)
                }
            }
        }
    }
}
