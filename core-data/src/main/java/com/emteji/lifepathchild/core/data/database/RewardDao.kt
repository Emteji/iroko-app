package com.emteji.lifepathchild.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emteji.lifepathchild.core.data.model.RewardEntity
import com.emteji.lifepathchild.core.data.model.RewardStatus

@Dao
interface RewardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reward: RewardEntity)

    @Query("SELECT * FROM rewards WHERE status = :status ORDER BY requestedAt DESC")
    suspend fun getRewardsByStatus(status: RewardStatus): List<RewardEntity>

    @Query("UPDATE rewards SET status = :newStatus, approvedAt = :timestamp WHERE id = :id")
    suspend fun updateStatus(id: String, newStatus: RewardStatus, timestamp: Long = System.currentTimeMillis())
}
