package com.emteji.lifepathchild.core.data.repository

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class PaymentConfigRow(
    val region: String,
    val provider: String,
    val enabled: Boolean
)

@Singleton
class PaymentConfigRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun getConfig(region: String): Result<PaymentConfigRow?> {
        return try {
            val rows = supabase.postgrest.from("payments_config").select {
                filter { eq("region", region) }
                limit(1)
            }.decodeList<PaymentConfigRow>()
            Result.success(rows.firstOrNull())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

