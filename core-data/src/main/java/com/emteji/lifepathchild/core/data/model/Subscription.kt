package com.emteji.lifepathchild.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class Subscription(
    @PrimaryKey
    val userId: String, // Parent ID
    val tier: SubscriptionTier = SubscriptionTier.FREE,
    val expiryDate: Long? = null, // Null = Lifetime or ongoing free
    val isActive: Boolean = true,
    val lastUpdated: Long = System.currentTimeMillis()
)

enum class SubscriptionTier {
    FREE,   // Entry level
    PAID,   // Core engine (Standard)
    PREMIUM // Power users
}
