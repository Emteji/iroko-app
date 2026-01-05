package com.emteji.lifepathchild.core.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class GuidanceRow(
    val child_id: String,
    val guidance_text: String?,
    val content: GuidanceContent?
)

@Serializable
data class GuidanceTask(
    val title: String,
    val description: String,
    val category: String,
    val duration_min: Int,
    val difficulty: String
)

@Serializable
data class GuidanceContent(
    val tasks: List<GuidanceTask>
)

@Singleton
class GuidanceRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun getLatestGuidanceText(childId: String): Result<String?> {
        return try {
            val rows = supabase.postgrest.from("daily_guidance")
                .select {
                    filter { eq("child_id", childId) }
                    order("created_at", Order.DESCENDING)
                    limit(1)
                }
                .decodeList<GuidanceRow>()
            Result.success(rows.firstOrNull()?.guidance_text)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLatestGuidanceContent(childId: String): Result<GuidanceContent?> {
        return try {
            val rows = supabase.postgrest.from("daily_guidance")
                .select {
                    filter { eq("child_id", childId) }
                    order("created_at", Order.DESCENDING)
                    limit(1)
                }
                .decodeList<GuidanceRow>()
            Result.success(rows.firstOrNull()?.content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
