package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.model.Mission
import com.emteji.lifepathchild.core.data.model.MissionStatus
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MissionsRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun getMissionsForChild(childId: String): Result<List<Mission>> {
        return try {
            val missions = supabase.postgrest.from("missions")
                .select {
                    filter {
                        eq("child_id", childId)
                    }
                }.decodeList<Mission>()
            Result.success(missions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createMission(mission: Mission): Result<Unit> {
        return try {
            supabase.postgrest.from("missions").insert(mission)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateStatus(missionId: String, status: MissionStatus, proofUrl: String? = null): Result<Unit> {
        return try {
            supabase.postgrest.from("missions").update({
                set("status", status)
                if (proofUrl != null) set("proof_url", proofUrl)
            }) {
                filter {
                    eq("id", missionId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
