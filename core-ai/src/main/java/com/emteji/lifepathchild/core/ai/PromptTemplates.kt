package com.emteji.lifepathchild.core.ai

import com.emteji.lifepathchild.core.ai.model.ChildInputProfile
import com.emteji.lifepathchild.core.ai.model.ChildActivityMetric

object PromptTemplates {

    // SECTION 6. PARENT PROMPT TEMPLATES

    fun dailyPlanningPrompt(child: ChildInputProfile): String {
        return """
            Given this child profile:
            - Name: ${child.coreData.name}
            - Age: ${child.coreData.age ?: "Unknown"}
            - Temperament: ${child.coreData.temperament ?: "Unknown"}
            - Academic Level: ${child.coreData.academicLevel ?: "Average"}
            - Behavioral Concerns: ${child.coreData.behavioralConcerns.joinToString(", ")}
            - Recent Activity: ${summarizeActivity(child.recentActivity)}
            
            Suggest three practical offline tasks today.
            Each task must develop discipline, thinking, or responsibility.
            Match difficulty to age and past consistency.
            Explain why each task matters in simple terms for the parent.
        """.trimIndent()
    }

    fun behaviorAnalysisPrompt(child: ChildInputProfile): String {
        return """
            Analyze this child’s profile and recent activity:
            - Temperament: ${child.coreData.temperament ?: "Unknown"}
            - Concerns: ${child.coreData.behavioralConcerns.joinToString(", ")}
            - Activity Log:
            ${summarizeActivity(child.recentActivity)}
            
            Identify patterns of strength and weakness based on their temperament.
            Highlight one specific growth area related to their 'Grit' or 'Omoluabi' score.
            Suggest one specific parenting adjustment the parent can make this week to support them.
        """.trimIndent()
    }

    fun recommendationPrompt(interests: List<String>): String {
        return """
            Based on observed interest patterns: ${interests.joinToString(", ")}
            Recommend practical learning tools.
            If physical toys are unavailable, provide offline household alternatives.
            Explain how each option builds skills.
        """.trimIndent()
    }

    fun reminderLogicPrompt(schedule: String): String {
        return """
            Given the parent schedule: $schedule
            Generate reminder times.
            Reminders must feel supportive, not nagging.
            Limit reminders to what improves follow through.
        """.trimIndent()
    }

    // SECTION 7. CHILD PROMPT TEMPLATES

    fun missionGenerationPrompt(child: ChildInputProfile): String {
        return """
            Create one mission suitable for this child today:
            - Name: ${child.coreData.name}
            - Age: ${child.coreData.age ?: "Unknown"}
            - Temperament: ${child.coreData.temperament ?: "Unknown"}
            
            The mission must be offline.
            It must take under 30 minutes.
            It must feel like a challenge, not punishment.
            Tone: Encouraging, Epic, Companion-like.
            Explain it simply.
        """.trimIndent()
    }

    fun encouragementPrompt(task: String): String {
        return """
            Respond to this completed task: "$task" with encouragement.
            Praise effort, not intelligence.
            Keep it short.
            Do not exaggerate.
        """.trimIndent()
    }

    // Helpers

    private fun summarizeActivity(activities: List<ChildActivityMetric>): String {
        if (activities.isEmpty()) return "No recent activity."
        return activities.takeLast(10).joinToString("\n") { 
            "- Task: ${it.taskType}, Completed: ${it.isCompleted}, Feedback: ${it.emotionalFeedback}" 
        }
    }
}
