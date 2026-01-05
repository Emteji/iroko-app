package com.emteji.lifepathchild.core.ai.model

import com.emteji.lifepathchild.core.data.model.Child
import com.emteji.lifepathchild.core.data.model.SubscriptionTier

/**
 * SECTION 3. AI INPUT CONTRACT
 * The AI only receives structured data.
 */

data class AIInputContext(
    val parentProfile: ParentInputProfile,
    val childProfile: ChildInputProfile,
    val systemStatus: SystemStatus
)

data class ParentInputProfile(
    val values: ParentValues,
    val rules: ParentRules,
    val reportedConcerns: List<ReportedConcern>
)

data class ParentValues(
    val disciplineLevel: DisciplineLevel = DisciplineLevel.MODERATE,
    val faithPreference: String = "Neutral", // e.g., Christian, Muslim, Traditional, Secular
    val ambitionLevel: AmbitionLevel = AmbitionLevel.BALANCED
)

enum class DisciplineLevel {
    STRICT, MODERATE, GENTLE
}

enum class AmbitionLevel {
    HIGH_ACHIEVER, BALANCED, HAPPINESS_FOCUSED
}

data class ParentRules(
    val screenTimeLimitMinutes: Int = 60,
    val taskLimitPerDay: Int = 3,
    val allowedRewardTypes: List<String> = emptyList() // e.g., "ScreenTime", "Treat", "Outing"
)

enum class ReportedConcern {
    BEHAVIOR_ISSUES,
    FEAR_ANXIETY,
    LAZINESS,
    LOW_CONFIDENCE,
    AGGRESSION,
    DISHONESTY,
    ENTITLEMENT
}

data class ChildInputProfile(
    val coreData: Child, // UPDATED: Now using the rich Child model
    val recentActivity: List<ChildActivityMetric>,
    val psychologicalProfile: PsychologicalProfile? = null
)

data class ChildActivityMetric(
    val taskId: String,
    val taskType: String,
    val isCompleted: Boolean,
    val timeTakenSeconds: Int,
    val repetitionCount: Int,
    val engagementDepth: EngagementDepth, // Inferred or reported
    val emotionalFeedback: EmotionalState // Happy, Bored, Stressed
)

enum class EngagementDepth {
    HIGH, MEDIUM, LOW, AVOIDANT
}

enum class EmotionalState {
    HAPPY, BORED, STRESSED, NEUTRAL, PROUD
}

data class SystemStatus(
    val subscriptionTier: SubscriptionTier,
    val isOffline: Boolean,
    val lastInteractionTimestamp: Long,
    val adminSafetyFlags: List<String> = emptyList()
)
