package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VillageContext(
    val id: String? = null,
    @SerialName("child_id") val childId: String,
    @SerialName("environment_type") val environmentType: String, // Urban, Rural, Diaspora
    @SerialName("value_weights") val valueWeights: Map<String, Int> = emptyMap(), // {"Respect": 10, "Cleanliness": 8}
    @SerialName("origin_region") val originRegion: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)
