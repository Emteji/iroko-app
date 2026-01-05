package com.emteji.lifepathchild.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class RewardEntity(
    @PrimaryKey
    val id: String,
    val title: String, // e.g., "15 min Screen Time"
    val cost: Int, // XP cost
    val status: RewardStatus = RewardStatus.PENDING,
    val requestedAt: Long = System.currentTimeMillis(),
    val approvedAt: Long? = null
)

enum class RewardStatus {
    PENDING, // "Delay Queue" / Penny Gap
    APPROVED,
    REJECTED,
    CLAIMED
}
