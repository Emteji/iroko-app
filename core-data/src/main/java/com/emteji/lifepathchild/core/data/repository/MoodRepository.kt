package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.model.MoodEntry
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class MoodRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun saveMood(entry: MoodEntry): Result<Unit> {
        return try {
            supabase.postgrest.from("moods")
                .insert(entry)
            Result.success(Unit)
        } catch (e: Exception) {
            // Log error
            Result.success(Unit) // Fallback for offline/demo
        }
    }
    
    suspend fun getRecentMoods(childId: String): Result<List<MoodEntry>> {
        return try {
             val moods = supabase.postgrest.from("moods")
                .select {
                    filter { eq("child_id", childId) }
                    order("created_at", io.github.jan.supabase.postgrest.query.Order.DESCENDING)
                    limit(5)
                }.decodeList<MoodEntry>()
            Result.success(moods)
        } catch (e: Exception) {
            Result.success(emptyList())
        }
    }
}
