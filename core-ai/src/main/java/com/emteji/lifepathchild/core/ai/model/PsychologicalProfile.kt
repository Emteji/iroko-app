package com.emteji.lifepathchild.core.ai.model

/**
 * SECTION 8. PSYCHOLOGICAL PATTERN ENGINE
 */

data class PsychologicalProfile(
    val archetype: ChildArchetype,
    val metrics: PsychologicalMetrics
)

enum class ChildArchetype {
    BUILDER,    // Likes creating, structure
    NEGOTIATOR, // Tests boundaries, persuasive
    EXPLORER,   // Curious, tries new things
    PERFORMER,  // Seeks validation, expressive
    OBSERVER,   // Watchful, cautious, detailed
    UNDEFINED
}

data class PsychologicalMetrics(
    val taskAvoidanceFreq: Float, // 0.0 to 1.0
    val completionSpeed: Float,   // Normalized speed score
    val preferenceClusters: List<String>, // e.g., ["Creative", "Physical"]
    val rewardSensitivity: Float, // 0.0 to 1.0
    val frustrationSignals: Int   // Count of "Stressed" feedback
)
