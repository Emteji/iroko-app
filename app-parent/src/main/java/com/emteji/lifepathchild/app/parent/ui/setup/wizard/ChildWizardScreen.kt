package com.emteji.lifepathchild.app.parent.ui.setup.wizard

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emteji.lifepathchild.app.parent.ui.components.IrokoButton
import com.emteji.lifepathchild.app.parent.ui.components.IrokoCard
import com.emteji.lifepathchild.app.parent.ui.components.IrokoDarkCard
import com.emteji.lifepathchild.app.parent.ui.theme.*

@Composable
fun ChildWizardScreen(
    onWizardComplete: (String) -> Unit, // Returns Child ID
    onBack: () -> Unit,
    viewModel: ChildWizardViewModel = hiltViewModel()
) {
    val step by viewModel.currentStep.collectAsState()
    val createdChildId by viewModel.createdChildId.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Auto-navigate when complete
    LaunchedEffect(createdChildId) {
        createdChildId?.let { onWizardComplete(it) }
    }

    Scaffold(
        containerColor = IrokoCream,
        topBar = {
            if (step > 0) {
                IconButton(onClick = { viewModel.previousStep() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = IrokoBrown)
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Background Texture (Optional)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ParchmentGradient)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Progress Bar
                LinearProgressIndicator(
                    progress = { (step + 1) / 5f },
                    modifier = Modifier.fillMaxWidth(),
                    color = IrokoGold,
                    trackColor = IrokoBrown.copy(alpha = 0.1f),
                )
                
                Spacer(modifier = Modifier.height(32.dp))

                // Content
                AnimatedContent(
                    targetState = step,
                    transitionSpec = {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut())
                    },
                    label = "WizardStep"
                ) { currentStep ->
                    when (currentStep) {
                        0 -> StepBasicInfo(viewModel)
                        1 -> StepTemperament(viewModel)
                        2 -> StepAcademics(viewModel)
                        3 -> StepConcerns(viewModel)
                        4 -> StepReview(viewModel)
                    }
                }
            }

            // Loading Overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = IrokoGold)
                }
            }
        }
    }
}

// --- STEPS ---

@Composable
fun StepBasicInfo(viewModel: ChildWizardViewModel) {
    val name by viewModel.name.collectAsState()
    val age by viewModel.age.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Let's meet your child.",
            style = MaterialTheme.typography.headlineMedium,
            color = IrokoBrown,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "We start with the basics to tailor the experience.",
            style = MaterialTheme.typography.bodyLarge,
            color = IrokoStone
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.setName(it) },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IrokoBrown,
                focusedLabelColor = IrokoBrown
            ),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = age,
            onValueChange = { if (it.length <= 2) viewModel.setAge(it.filter { c -> c.isDigit() }) },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IrokoBrown,
                focusedLabelColor = IrokoBrown
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        IrokoButton(
            text = "Continue",
            onClick = { viewModel.nextStep() },
            enabled = name.isNotEmpty() && age.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StepTemperament(viewModel: ChildWizardViewModel) {
    val selected by viewModel.temperament.collectAsState()
    val options = listOf("Choleric (Strong-willed)", "Sanguine (Social)", "Phlegmatic (Calm)", "Melancholic (Thoughtful)")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "What is their nature?",
            style = MaterialTheme.typography.headlineMedium,
            color = IrokoBrown,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "This helps the AI choose the right tone.",
            style = MaterialTheme.typography.bodyLarge,
            color = IrokoStone
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        options.forEach { option ->
            val isSelected = selected == option
            IrokoCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable { viewModel.setTemperament(option) },
                backgroundColor = if (isSelected) IrokoBrown else IrokoWhite,
                elevation = if (isSelected) 8.dp else 2.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        color = if (isSelected) IrokoGold else IrokoBrown,
                        fontWeight = FontWeight.Bold
                    )
                    if (isSelected) {
                        Icon(Icons.Filled.Check, contentDescription = null, tint = IrokoGold)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        IrokoButton(
            text = "Continue",
            onClick = { viewModel.nextStep() },
            enabled = selected != null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StepAcademics(viewModel: ChildWizardViewModel) {
    val selected by viewModel.academicLevel.collectAsState()
    val options = listOf("Needs Support", "Average", "Above Average", "Gifted")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Academic Standing",
            style = MaterialTheme.typography.headlineMedium,
            color = IrokoBrown,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(options) { option ->
                val isSelected = selected == option
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) IrokoBrown else IrokoWhite)
                        .clickable { viewModel.setAcademicLevel(option) }
                        .padding(16.dp)
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        textAlign = TextAlign.Center,
                        color = if (isSelected) IrokoGold else IrokoBrown,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        IrokoButton(
            text = "Continue",
            onClick = { viewModel.nextStep() },
            enabled = selected != null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StepConcerns(viewModel: ChildWizardViewModel) {
    val selected by viewModel.behavioralConcerns.collectAsState()
    val options = listOf("Screen Addiction", "Lack of Focus", "Disrespect", "Laziness", "Anxiety", "Anger Issues")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Any Concerns?",
            style = MaterialTheme.typography.headlineMedium,
            color = IrokoBrown,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Select all that apply. We will help you address these.",
            style = MaterialTheme.typography.bodyLarge,
            color = IrokoStone
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        options.forEach { option ->
            val isSelected = selected.contains(option)
            FilterChip(
                selected = isSelected,
                onClick = { viewModel.toggleConcern(option) },
                label = { Text(option) },
                modifier = Modifier.padding(bottom = 8.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = IrokoBrown,
                    selectedLabelColor = IrokoGold,
                    containerColor = IrokoWhite,
                    labelColor = IrokoBrown
                )
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        IrokoButton(
            text = "Continue",
            onClick = { viewModel.nextStep() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StepReview(viewModel: ChildWizardViewModel) {
    val name by viewModel.name.collectAsState()
    val age by viewModel.age.collectAsState()
    val temp by viewModel.temperament.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ready to Begin?",
            style = MaterialTheme.typography.headlineMedium,
            color = IrokoBrown,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        IrokoDarkCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text("CHILD PROFILE", color = IrokoGold, style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("$name, $age years old", color = IrokoWhite, style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(temp ?: "Unknown", color = IrokoWhite.copy(alpha = 0.7f))
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "By creating this profile, you allow IROKO's AI to customize the curriculum for $name.",
            style = MaterialTheme.typography.bodyMedium,
            color = IrokoStone,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        IrokoButton(
            text = "Create Profile",
            onClick = { viewModel.submitProfile() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
