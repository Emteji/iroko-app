package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Mission(
    val id: String,
    @SerialName("child_id") val childId: String,
    val type: MissionType,
    val description: String,
    val status: MissionStatus,
    @SerialName("proof_url") val proofUrl: String? = null,
    @SerialName("persona_used") val personaUsed: String? = null,
    @SerialName("created_at") val createdAt: String
)

@Serializable
enum class MissionType {
    @SerialName("grit") GRIT,
    @SerialName("money") MONEY,
    @SerialName("respect") RESPECT,
    @SerialName("street_smarts") STREET_SMARTS
}

@Serializable
enum class MissionStatus {
    @SerialName("assigned") ASSIGNED,
    @SerialName("completed") COMPLETED,
    @SerialName("verified") VERIFIED
}
