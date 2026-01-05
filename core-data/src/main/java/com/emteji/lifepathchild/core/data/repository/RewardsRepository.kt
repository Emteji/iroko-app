package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.model.Reward
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardsRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun getAvailableRewards(childId: String): Result<List<Reward>> {
        // Complex query: We need rewards from the parent linked to this child
        // For simplicity in MVP, we might fetch rewards where parent_id matches the child's parent
        // However, standard RLS "Children can view parent rewards" handles the filtering on the backend.
        // So we just query 'rewards' and Supabase returns what is allowed.
        return try {
            val rewards = supabase.postgrest.from("reward_catalog")
                .select()
                .decodeList<Reward>()
            Result.success(rewards)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun requestSpend(walletId: String, childId: String, parentId: String, amountPoints: Int): Result<Unit> {
        return try {
            supabase.postgrest.from("reward_transactions").insert(
                mapOf(
                    "wallet_id" to walletId,
                    "child_id" to childId,
                    "parent_id" to parentId,
                    "type" to "spend",
                    "amount_points" to amountPoints,
                    "source" to "spend_request",
                    "status" to "pending"
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
