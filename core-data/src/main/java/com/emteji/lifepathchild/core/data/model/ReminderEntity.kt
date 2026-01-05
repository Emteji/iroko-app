package com.emteji.lifepathchild.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: ReminderType,
    val scheduledTime: Long,
    val message: String,
    val isEnabled: Boolean = true,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ReminderType {
    DAILY_MORNING,
    DAILY_EVENING,
    WEEKLY_GROWTH,
    WEEKLY_DISCIPLINE,
    MONTHLY_REVIEW
}
