package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChildSession(
    val id: String,
    @SerialName("child_id") val childId: String,
    @SerialName("device_id") val deviceId: String,
    @SerialName("session_end") val sessionEnd: String? = null,
    @SerialName("is_active") val isActive: Boolean = true,
    val revoked: Boolean = false
)

