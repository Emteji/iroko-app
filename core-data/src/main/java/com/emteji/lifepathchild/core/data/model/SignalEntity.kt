package com.emteji.lifepathchild.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signals")
data class SignalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: SignalType,
    val target: String, // The skill or topic (e.g. "counting", "blocks")
    val value: Float, // Duration in seconds, or weight
    val context: String? = null, // JSON or simple string metadata
    val timestamp: Long = System.currentTimeMillis()
)

enum class SignalType {
    CHOICE,  // Selection preference
    TIME,    // Duration spent
    EFFORT,  // Retries, persistence
    EMOTION, // Reaction, speed (implied emotion)
    TEMPERAMENT // Temperament indicators
}
