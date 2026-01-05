package com.emteji.lifepathchild.core.analytics

import com.emteji.lifepathchild.core.data.model.SignalType
import com.emteji.lifepathchild.core.data.repository.SignalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterestEngine @Inject constructor(
    private val signalRepository: SignalRepository
) {

    /**
     * Submit a raw signal from an application interaction.
     * 
     * @param type The type of signal (CHOICE, TIME, EFFORT, EMOTION)
     * @param target The target topic or skill (e.g., "counting", "blocks")
     * @param value The raw value (e.g., duration in seconds, count, or rating)
     * @param context Optional metadata
     */
    suspend fun submitSignal(type: SignalType, target: String, value: Float, context: String? = null) {
        signalRepository.logSignal(type, target, value, context)
        // In a real system, we might trigger re-calc here or just log it for batch processing
    }

    /**
     * Calculate the current interest score for a given target.
     * 
     * Scoring Weights (Derived from Rules):
     * - Choice: Weighted sum of choices.
     * - Time: Multiplier based on duration. Longer time = deeper interest.
     * - Effort: High weight. Effort > Success.
     * - Emotion: Modifier (currently inferred from speed/repetition).
     */
    suspend fun calculateInterestAndTrends(target: String): InterestResult {
        val signals = signalRepository.getSignalsForTarget(target)
        
        if (signals.isEmpty()) return InterestResult(target, 0f, "New", SignalType.CHOICE, InterestStatus.NEUTRAL)

        var baseScore = 0.0f
        var effortBonus = 0.0f
        var timeMultiplier = 1.0f
        
        // Confirmation Metrics
        val firstSignalTime = signals.minOf { it.timestamp }
        val distinctSourceTypes = mutableSetOf<String>()
        val weeksActive = (System.currentTimeMillis() - firstSignalTime) / (1000 * 60 * 60 * 24 * 7)

        signals.forEach { signal ->
            // Parse Source Type from context (convention: "Source: TYPE")
            // Or simple heuristic if context contains keywords
            val context = signal.context ?: ""
            if (context.contains("Mission", true)) distinctSourceTypes.add("MICRO_TASK")
            if (context.contains("Life", true) || context.contains("Market", true)) distinctSourceTypes.add("LIFE_TASK")
            if (context.contains("Simulation", true) || context.contains("Scenario", true)) distinctSourceTypes.add("SIMULATION")
            if (context.contains("Question", true)) distinctSourceTypes.add("QUESTION")

            // Layer 2: Pattern Scoring - Time Decay
            // "Scores decay weekly. Recent behavior matters."
            val ageInWeeks = (System.currentTimeMillis() - signal.timestamp) / (1000f * 60 * 60 * 24 * 7)
            // Decay factor: 0.9 per week old. 
            // e.g. 0 weeks = 1.0, 1 week = 0.9, 4 weeks = 0.65
            val ageWeight = Math.pow(0.9, ageInWeeks.toDouble()).toFloat()

            when (signal.type) {
                SignalType.CHOICE -> {
                    baseScore += (5.0f * ageWeight)
                }
                SignalType.TIME -> {
                    val videoMinutes = signal.value / 60
                    if (videoMinutes > 5) {
                        timeMultiplier += (0.1f * ageWeight)
                    }
                    baseScore += (videoMinutes * 0.5f * ageWeight) 
                }
                SignalType.EFFORT -> {
                    effortBonus += (signal.value * 3.0f * ageWeight) 
                }
                SignalType.EMOTION -> {
                    if (signal.value > 0.8) {
                        baseScore += (2.0f * ageWeight)
                    }
                }
                SignalType.TEMPERAMENT -> {
                    // Temperament doesn't directly add to interest score but validates it
                    // Maybe small bonus for "Risk taking" in Leadership? 
                    // Keeping neutral for now.
                }
            }
        }

        val totalScore = (baseScore + effortBonus) * timeMultiplier
        
        // Confirmation Rule: 4 Weeks + 3 Task Types
        val isConfirmed = weeksActive >= 4 && distinctSourceTypes.size >= 3
        
        val status = if (isConfirmed) InterestStatus.CONFIRMED else InterestStatus.EMERGING
        // Override if score is very low
        val finalStatus = if (totalScore < 10) InterestStatus.NEUTRAL else status

        return InterestResult(
            target = target,
            score = totalScore,
            trend = if (totalScore > 50) "High Interest" else "Emerging",
            dominantSignal = SignalType.TIME,
            status = finalStatus
        )
    }
}

data class InterestResult(
    val target: String,
    val score: Float,
    val trend: String,
    val dominantSignal: SignalType,
    val status: InterestStatus
)

enum class InterestStatus {
    NEUTRAL,
    EMERGING,
    CONFIRMED
}
