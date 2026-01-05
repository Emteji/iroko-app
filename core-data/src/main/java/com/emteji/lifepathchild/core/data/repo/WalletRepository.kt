package com.emteji.lifepathchild.core.data.repo

import com.emteji.lifepathchild.core.auth.SupabaseProvider
import com.emteji.lifepathchild.core.data.model.RewardsWallet
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepository @Inject constructor(provider: SupabaseProvider) {
    private val client = provider.client

    suspend fun walletForChild(childId: String): RewardsWallet? {
        val res = client.postgrest["rewards_wallet"].select {
            filter { eq("child_id", childId) }
            limit(1)
        }
        val list = res.decodeList<RewardsWallet>()
        return list.firstOrNull()
    }
}
