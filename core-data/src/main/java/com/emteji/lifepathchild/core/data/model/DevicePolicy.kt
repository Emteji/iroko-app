package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DevicePolicy(
    @SerialName("child_id") val childId: String,
    @SerialName("daily_limit_minutes") val dailyLimitMinutes: Int = 120,
    @SerialName("is_locked") val isLocked: Boolean = false,
    @SerialName("bedtime_start") val bedtimeStart: String = "21:00",
    @SerialName("bedtime_end") val bedtimeEnd: String = "07:00",
    @SerialName("emergency_unlock_pin") val emergencyUnlockPin: String? = null
)
