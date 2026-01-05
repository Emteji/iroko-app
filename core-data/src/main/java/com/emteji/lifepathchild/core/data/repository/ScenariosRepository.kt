package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.model.Scenario
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScenariosRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun getScenariosForChild(childId: String): Result<List<Scenario>> {
        return try {
            val scenarios = supabase.postgrest.from("scenarios")
                .select {
                    filter {
                        eq("child_id", childId)
                    }
                }.decodeList<Scenario>()
            Result.success(scenarios)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitChoice(scenarioId: String, outcomeFeedback: String): Result<Unit> {
        return try {
            supabase.postgrest.from("scenarios").update({
                set("outcome_feedback", outcomeFeedback)
            }) {
                filter {
                    eq("id", scenarioId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
