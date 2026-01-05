package com.emteji.lifepathchild.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "micro_task_progress")
data class MicroTaskProgress(
    @PrimaryKey
    val taskId: String,
    val isCompleted: Boolean = false,
    val outcome: String? = null, // "Quick Success", "Struggled", "Refused"
    val completedAt: Long? = null
)
