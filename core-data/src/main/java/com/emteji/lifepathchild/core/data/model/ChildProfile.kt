package com.emteji.lifepathchild.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_profiles")
data class ChildProfile(
    @PrimaryKey
    val id: String,
    val parentId: String = "",
    val name: String,
    val dateOfBirth: Long? = null,
    val gender: String? = null,
    val villageContext: String? = null,
    val gritScore: Int = 0,
    val xpTotal: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
