package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RewardsWallet(
    val id: String,
    @SerialName("child_id") val childId: String,
    @SerialName("points_balance") val pointsBalance: Int = 0,
    @SerialName("saved_balance") val savedBalance: Double = 0.0
)

