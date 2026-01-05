package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SystemUpdate(
    val id: String = "",
    val title: String,
    val content: String,
    val target_audience: String, // "all", "parent", "child"
    val is_published: Boolean = false,
    val created_at: String? = null
)
