package com.emteji.lifepathchild.core.analytics

import com.emteji.lifepathchild.core.ai.model.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SECTION 8: PSYCHOLOGICAL PATTERN ENGINE
 * Tracks behavioral metrics to infer child archetypes.
 * "These types influence task framing. They never label the child permanently."
 */
@Singleton
class PsychologicalEngine @Inject constructor() {

    fun analyze(metrics: List<ChildActivityMetric>): PsychologicalProfile {
        if (metrics.isEmpty()) {
            return PsychologicalProfile(
                archetype = ChildArchetype.UNDEFINED,
                metrics = PsychologicalMetrics(0f, 0f, emptyList(), 0f, 0)
            )
        }

        val total = metrics.size.toFloat()
        val avoidanceCount = metrics.count { it.engagementDepth == EngagementDepth.AVOIDANT }
        val completionCount = metrics.count { it.isCompleted }
        val frustrationCount = metrics.count { it.emotionalFeedback == EmotionalState.STRESSED }
        val happinessCount = metrics.count { it.emotionalFeedback == EmotionalState.HAPPY }

        val taskAvoidanceFreq = if (total > 0) avoidanceCount / total else 0f
        
        // Determine Archetype based on simplified heuristics
        // In a full implementation, this would use clustering on 'preferenceClusters' and historical trends.
        
        val archetype = determineArchetype(taskAvoidanceFreq, completionCount, total, frustrationCount)

        return PsychologicalProfile(
            archetype = archetype,
            metrics = PsychologicalMetrics(
                taskAvoidanceFreq = taskAvoidanceFreq,
                completionSpeed = 0.5f, // Placeholder: requires start/end timestamps analysis
                preferenceClusters = inferPreferences(metrics),
                rewardSensitivity = 0.5f, // Placeholder: requires reward redemption history
                frustrationSignals = frustrationCount
            )
        )
    }

    private fun determineArchetype(
        avoidanceFreq: Float, 
        completionCount: Int, 
        total: Float, 
        frustrationCount: Int
    ): ChildArchetype {
        val completionRate = completionCount / total
        
        return when {
            // Negotiator: High avoidance, tests boundaries
            avoidanceFreq > 0.3f -> ChildArchetype.NEGOTIATOR
            
            // Builder: High completion, consistent
            completionRate > 0.7f -> ChildArchetype.BUILDER
            
            // Observer: Low completion, but low frustration (just watching/thinking)? 
            // Or maybe low output but high time spent? (Hard to distinguish without more data)
            completionRate < 0.4f && frustrationCount == 0 -> ChildArchetype.OBSERVER
            
            // Performer: (Requires social feedback signals, placeholder)
            
            // Explorer: Default for balanced stats
            else -> ChildArchetype.EXPLORER
        }
    }

    private fun inferPreferences(metrics: List<ChildActivityMetric>): List<String> {
        // Group by task type and count happy/high engagement
        return metrics
            .filter { it.emotionalFeedback == EmotionalState.HAPPY || it.engagementDepth == EngagementDepth.HIGH }
            .map { it.taskType }
            .distinct()
    }
}
