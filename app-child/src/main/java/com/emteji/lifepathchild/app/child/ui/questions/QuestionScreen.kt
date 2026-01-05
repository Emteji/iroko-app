package com.emteji.lifepathchild.app.child.ui.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.analytics.InterestEngine
import com.emteji.lifepathchild.core.data.model.SignalType
import com.emteji.lifepathchild.core.ui.components.BinaryChoiceCard
import com.emteji.lifepathchild.core.ui.components.BinaryOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val interestEngine: InterestEngine
) : ViewModel() {

    fun onChoiceSelected(option: BinaryOption, category: String) {
        viewModelScope.launch {
            interestEngine.submitSignal(
                type = SignalType.CHOICE,
                target = option.id, // Using option ID as the "topic" (e.g. "blocks")
                value = 1.0f,
                context = "Question: $category"
            )
        }
    }
}

@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel = hiltViewModel()
) {
    // Hardcoded for demo - in real app would come from a Question Repository
    val optionA = BinaryOption(id = "blocks", text = "Build Something", color = Color(0xFFBBDEFB), iconEmoji = "🧱")
    val optionB = BinaryOption(id = "story", text = "Read a Story", color = Color(0xFFC8E6C9), iconEmoji = "📚")

    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (selectedOption == null) {
            Text(
                text = "What do you want to play today?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F), // TextPrimary
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            BinaryChoiceCard(
                optionA = optionA,
                optionB = optionB,
                onOptionSelected = {
                    selectedOption = it.text
                    viewModel.onChoiceSelected(it, "Daily Activity")
                }
            )
        } else {
            // Confirmation State
            Text(
                text = "Great choice!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00C853)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Let's $selectedOption!",
                fontSize = 24.sp,
                color = Color.Gray
            )
        }
    }
}
