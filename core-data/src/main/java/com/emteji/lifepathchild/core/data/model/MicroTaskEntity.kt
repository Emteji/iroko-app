package com.emteji.lifepathchild.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "micro_tasks")
data class MicroTaskEntity(
    @PrimaryKey
    val id: String, // e.g. "math_l1", "social_l2"
    val category: TaskCategory,
    val level: Int, // 1 (Simple), 2 (Variable), 3 (Complex)
    val title: String,
    val description: String,
    val requiredLevel: Int = level - 1, // Default dependency is previous level
    val tags: List<String> = emptyList() // e.g. "no-toy", "outdoor"
)

enum class TaskCategory {
    MATH,
    SOCIAL,
    LEADERSHIP
}
