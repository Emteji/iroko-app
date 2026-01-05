package com.emteji.lifepathchild.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parent_profiles")
data class ParentProfile(
    @PrimaryKey
    val id: String, // Auth User ID
    val email: String?,
    val setupCompleted: Boolean = false,
    val primaryCulturalContext: String = "Home", // "Home" or "Diaspora"
    val createdAt: Long = System.currentTimeMillis()
)
