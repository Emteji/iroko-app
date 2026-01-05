package com.emteji.lifepathchild.core.payments

enum class PlanType(val id: String) {
    FREE("free"),
    STANDARD("standard"),
    PREMIUM("premium")
}

data class Subscription(
    val userId: String,
    val plan: PlanType,
    val isActive: Boolean,
    val expiresAt: Long?
)
