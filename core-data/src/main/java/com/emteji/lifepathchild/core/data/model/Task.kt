package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: String = "",
    @SerialName("child_id") val childId: String,
    val title: String, // e.g., "Clean your room"
    val description: String? = null,
    val category: String = "General", // Household, Academic, etc.
    val difficulty: String = "Medium", // Easy, Medium, Hard
    @SerialName("reward_points") val rewardPoints: Int = 10,
    @SerialName("is_unlock_condition") val isUnlockCondition: Boolean = false,
    @SerialName("due_date") val dueDate: String? = null, // ISO8601
    val status: TaskStatus = TaskStatus.PENDING,
    @SerialName("created_at") val createdAt: String? = null,
    
    // AI & Psychological Metadata
    @SerialName("ai_generated") val aiGenerated: Boolean = false,
    @SerialName("psychological_method") val psychologicalMethod: String? = null, // e.g., "Positive Reinforcement", "Habit Stacking"
    @SerialName("skill_trained") val skillTrained: String? = null, // e.g., "Delayed Gratification", "Organization"
    @SerialName("expected_outcome") val expectedOutcome: String? = null // e.g., "Increases focus duration by 10%"
)

@Serializable
enum class TaskStatus {
    @SerialName("pending") PENDING,
    @SerialName("completed") COMPLETED,
    @SerialName("verified") VERIFIED
}

