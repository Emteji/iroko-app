package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Reward(
    val id: String,
    @SerialName("parent_id") val parentId: String,
    val description: String,
    @SerialName("xp_cost") val xpCost: Int,
    val type: RewardType,
    @SerialName("is_redeemed") val isRedeemed: Boolean = false
)

@Serializable
enum class RewardType {
    @SerialName("grit") GRIT,
    @SerialName("omoluabi") OMOLUABI
}
