package com.emteji.lifepathchild.core.ai.model

/**
 * SECTION 4. AI OUTPUT CONTRACT
 * The AI produces only these outputs.
 */

sealed class AIOutput

// For Parents
data class ParentGuidanceOutput(
    val dailyPlan: DailyPlan? = null,
    val weeklyGrowthReport: GrowthReport? = null,
    val behaviorInsights: List<BehaviorInsight> = emptyList(),
    val recommendations: List<Recommendation> = emptyList(),
    val alerts: List<SafetyAlert> = emptyList()
) : AIOutput()

data class DailyPlan(
    val date: String,
    val suggestedTasks: List<TaskSuggestion>
)

data class TaskSuggestion(
    val title: String,
    val description: String,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val reasonForParent: String, // Why this task matters
    val estimatedDurationMinutes: Int
)

data class GrowthReport(
    val periodStart: String,
    val periodEnd: String,
    val summary: String,
    val strengthAreas: List<String>,
    val improvementAreas: List<String>
)

data class BehaviorInsight(
    val observation: String,
    val suggestion: String, // Discipline guidance phrased calmly
    val relatedConcern: String? = null
)

data class Recommendation(
    val type: RecommendationType,
    val name: String,
    val description: String,
    val skillBuilt: String
)

enum class RecommendationType {
    TOY, ACTIVITY, HOUSEHOLD_ALTERNATIVE
}

data class SafetyAlert(
    val severity: AlertSeverity,
    val message: String,
    val actionRequired: Boolean
)

enum class AlertSeverity {
    INFO, WARNING, CRITICAL
}

// For Children
data class ChildInteractionOutput(
    val mission: Mission? = null,
    val story: String? = null,
    val encouragement: String? = null
) : AIOutput()

data class Mission(
    val title: String,
    val description: String, // Simple explanation
    val isOffline: Boolean = true,
    val durationMinutes: Int,
    val challengeLevel: String // "Fun", "Tricky", "Heroic"
)
