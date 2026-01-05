package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoodEntry(
    @SerialName("id") val id: String = "",
    @SerialName("child_id") val childId: String,
    @SerialName("mood_label") val moodLabel: String,
    @SerialName("created_at") val createdAt: Long = System.currentTimeMillis()
)
