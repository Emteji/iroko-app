package com.emteji.lifepathchild.core.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

data class Plan(
    val id: String,
    val name: String,
    val price_monthly: Double?,
    val price_yearly: Double?
)

@Singleton
class PlansRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun getPlans(): Result<List<Plan>> {
        return try {
            val plans = supabase.postgrest.from("plans").select().decodeList<Plan>()
            Result.success(plans)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

