package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Scenario(
    val id: String,
    @SerialName("child_id") val childId: String,
    val description: String,
    val choices: List<ScenarioChoice>,
    @SerialName("outcome_feedback") val outcomeFeedback: String? = null,
    @SerialName("persona_used") val personaUsed: String? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
data class ScenarioChoice(
    val id: String,
    val text: String,
    @SerialName("outcome_type") val outcomeType: String
)
