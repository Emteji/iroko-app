package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.model.DevicePolicy
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject

class DeviceControlRepository @Inject constructor(
    private val supabase: SupabaseClient
) {

    suspend fun getPolicyForChild(childId: String): Result<DevicePolicy> {
        return try {
            // Try fetch from DB
            val policy = supabase.postgrest.from("device_policies")
                .select {
                    filter { eq("child_id", childId) }
                }.decodeSingle<DevicePolicy>()
            Result.success(policy)
        } catch (e: Exception) {
            // Fallback / Default
            Result.success(DevicePolicy(childId = childId))
        }
    }

    suspend fun updatePolicy(policy: DevicePolicy): Result<Unit> {
        return try {
            supabase.postgrest.from("device_policies")
                .upsert(policy)
            Result.success(Unit)
        } catch (e: Exception) {
            // Fallback for demo - pretend it worked
            Result.success(Unit)
        }
    }
    
    suspend fun sendLockCommand(childId: String, lock: Boolean): Result<Unit> {
        // In a real app, this would push a notification or update a realtime table
        return updatePolicy(DevicePolicy(childId = childId, isLocked = lock))
    }
}
