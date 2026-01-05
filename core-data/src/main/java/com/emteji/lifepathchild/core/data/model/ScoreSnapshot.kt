package com.emteji.lifepathchild.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_snapshots")
data class ScoreSnapshot(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val childId: String,
    val weekStart: Long, // Timestamp of start of week
    val focusScore: Float,
    val disciplineScore: Float,
    val confidenceScore: Float,
    val overallTrend: String, // "Rising", "Stable", "Falling"
    val timestamp: Long = System.currentTimeMillis()
)
