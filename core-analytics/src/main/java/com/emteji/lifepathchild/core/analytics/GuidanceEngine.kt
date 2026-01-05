package com.emteji.lifepathchild.core.analytics

import com.emteji.lifepathchild.core.ai.AIService
import com.emteji.lifepathchild.core.ai.model.*
import com.emteji.lifepathchild.core.data.model.Child
import com.emteji.lifepathchild.core.data.model.SubscriptionTier
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Deprecated("Use ParentGuidanceOutput")
data class DailyGuidance(
    val parentTask: String,
    val observationFocus: String,
    val childTask: String? = null
)

@Singleton
class GuidanceEngine @Inject constructor(
    private val interestEngine: InterestEngine,
    private val aiService: AIService,
    private val psychologicalEngine: PsychologicalEngine
) {

    /**
     * Layer 4: Guidance Generation
     * "AI instructs parents, not children."
     * 
     * Output: Structured ParentGuidanceOutput.
     */
    suspend fun generateDailyPlan(child: Child): ParentGuidanceOutput {
        val input = createDefaultContext(child)
        return generateDailyPlan(input)
    }

    suspend fun generateDailyPlan(input: AIInputContext): ParentGuidanceOutput {
        // 1. Generate text from AI Service
        val result = aiService.generateDailyPlan(input.childProfile)
        
        // 2. Parse result or fallback
        return if (result.isSuccess) {
            val responseText = result.getOrNull() ?: ""
            parseResponseToOutput(responseText)
        } else {
            createFallbackOutput()
        }
    }

    // Bridge for backward compatibility if needed, but better to migrate.
    // Keeping this overload to avoid breaking ParentDashboardViewModel immediately if I can't update it in same turn,
    // but I will update it.
    
    private fun createDefaultContext(child: Child): AIInputContext {
        val recentActivity = emptyList<ChildActivityMetric>() // TODO: Inject Repository to fetch activity
        val psychoProfile = psychologicalEngine.analyze(recentActivity)

        return AIInputContext(
            parentProfile = ParentInputProfile(
                values = ParentValues(),
                rules = ParentRules(),
                reportedConcerns = emptyList()
            ),
            childProfile = ChildInputProfile(
                coreData = child,
                recentActivity = recentActivity,
                psychologicalProfile = psychoProfile
            ),
            systemStatus = SystemStatus(
                subscriptionTier = SubscriptionTier.FREE,
                isOffline = false,
                lastInteractionTimestamp = System.currentTimeMillis()
            )
        )
    }

    private fun parseResponseToOutput(text: String): ParentGuidanceOutput {
        // Simple parsing logic since the prompt asks for "Suggest three practical offline tasks..."
        // We will treat the text as a list of suggestions.
        
        val tasks = parseTasksFromText(text)
        
        val dailyPlan = DailyPlan(
            date = LocalDate.now().toString(),
            suggestedTasks = tasks
        )
        
        return ParentGuidanceOutput(dailyPlan = dailyPlan)
    }

    private fun parseTasksFromText(text: String): List<TaskSuggestion> {
        // Heuristic parser
        // If text is unstructured, we wrap it in one task.
        
        return listOf(
            TaskSuggestion(
                title = "Daily Guidance",
                description = text,
                difficulty = "Adaptive",
                reasonForParent = "AI Suggested based on profile",
                estimatedDurationMinutes = 20
            )
        )
    }

    private fun createFallbackOutput(): ParentGuidanceOutput {
        return ParentGuidanceOutput(
            dailyPlan = DailyPlan(
                date = LocalDate.now().toString(),
                suggestedTasks = listOf(
                    TaskSuggestion(
                        title = "Connect with your child",
                        description = "Spend 15 minutes playing offline.",
                        difficulty = "Easy",
                        reasonForParent = "Building bond.",
                        estimatedDurationMinutes = 15
                    )
                )
            )
        )
    }
}
