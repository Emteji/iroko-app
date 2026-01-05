package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.database.RewardDao
import com.emteji.lifepathchild.core.data.model.RewardEntity
import com.emteji.lifepathchild.core.data.model.RewardStatus
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardRepository @Inject constructor(
    private val rewardDao: RewardDao
) {
    // Child requests a reward -> Goes to Delay Queue (PENDING)
    suspend fun requestReward(title: String, cost: Int) {
        val reward = RewardEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            cost = cost,
            status = RewardStatus.PENDING
        )
        rewardDao.insert(reward)
    }

    // Parent fetches Pending Queue
    suspend fun getPendingRewards(): List<RewardEntity> {
        return rewardDao.getRewardsByStatus(RewardStatus.PENDING)
    }

    // Parent Approves -> Moves out of Queue
    suspend fun approveReward(id: String) {
        rewardDao.updateStatus(id, RewardStatus.APPROVED)
    }

    suspend fun rejectReward(id: String) {
        rewardDao.updateStatus(id, RewardStatus.REJECTED)
    }
}
