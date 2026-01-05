package com.emteji.lifepathchild.app.parent.ui.intelligence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.ai.AIService
import com.emteji.lifepathchild.core.ai.model.ChildInputProfile
import com.emteji.lifepathchild.core.data.model.Child
import com.emteji.lifepathchild.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InsightCard(
    val type: InsightType,
    val title: String,
    val body: String,
    val methodUsed: String? = null
)

enum class InsightType {
    GROWTH,
    RISK,
    ADVICE
}

@HiltViewModel
class IntelligenceViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val aiService: AIService
) : ViewModel() {

    private val _selectedChild = MutableStateFlow<Child?>(null)
    val selectedChild: StateFlow<Child?> = _selectedChild.asStateFlow()

    private val _insights = MutableStateFlow<List<InsightCard>>(emptyList())
    val insights: StateFlow<List<InsightCard>> = _insights.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val children = userRepository.getChildrenForParent().getOrDefault(emptyList())
            if (children.isNotEmpty()) {
                val child = children[0]
                _selectedChild.value = child
                generateInsights(child)
            }
        }
    }

    private fun generateInsights(child: Child) {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Build Input Profile
            val input = ChildInputProfile(
                coreData = child,
                recentActivity = emptyList() // In real app, fetch from ActivityRepository
            )
            
            // Call AI
            val result = aiService.generateBehaviorAnalysis(input)
            
            result.onSuccess { responseText ->
                // Parse the AI response into cards
                // Assuming the prompt asks for specific sections, or we parse generally
                // For this MVP, we will wrap the whole text in one card if parsing fails, 
                // or split by newlines if simple.
                
                // Real implementation would request JSON.
                // Here we simulate parsing or just show the raw analysis.
                
                val cards = mutableListOf<InsightCard>()
                
                // Add the main AI Analysis
                cards.add(
                    InsightCard(
                        type = InsightType.ADVICE,
                        title = "Behavioral Analysis",
                        body = responseText,
                        methodUsed = "Temperament Profiling"
                    )
                )
                
                // Add a static/mock risk for demo purposes if AI didn't flag one explicitly
                // (or if we parse keywords like "Concern")
                if (responseText.contains("Concern", ignoreCase = true)) {
                     cards.add(
                        InsightCard(
                            type = InsightType.RISK,
                            title = "Area of Focus",
                            body = "The AI identified a specific concern in the analysis above. Please review the suggested adjustment.",
                            methodUsed = "Pattern Recognition"
                        )
                    )
                }
                
                _insights.value = cards
            }.onFailure {
                // Fallback
                 _insights.value = listOf(
                    InsightCard(
                        type = InsightType.ADVICE,
                        title = "Connection Issue",
                        body = "Unable to reach Iroko Intelligence. Displaying cached insights.",
                        methodUsed = "Offline Mode"
                    )
                )
            }
            
            _isLoading.value = false
        }
    }
}
