package com.emteji.lifepathchild.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emteji.lifepathchild.core.data.model.ChildProfile
import com.emteji.lifepathchild.core.data.model.ParentProfile
import com.emteji.lifepathchild.core.data.model.ScoreSnapshot
import com.emteji.lifepathchild.core.data.model.Subscription
import com.emteji.lifepathchild.core.data.model.SubscriptionTier

@Dao
interface UserDao {
    // Parent Profile
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParent(profile: ParentProfile)

    @Query("SELECT * FROM parent_profiles LIMIT 1") // MVP assumes single parent user per device
    suspend fun getParentProfile(): ParentProfile?

    // Child Profile
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChild(profile: ChildProfile)

    @Query("SELECT * FROM child_profiles WHERE parentId = :parentId")
    suspend fun getChildrenForParent(parentId: String): List<ChildProfile>

    @Query("SELECT * FROM child_profiles WHERE id = :childId")
    suspend fun getChildById(childId: String): ChildProfile?

    // Subscription
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSubscription(subscription: Subscription)

    @Query("UPDATE subscriptions SET tier = :tier, lastUpdated = :timestamp WHERE userId = :userId")
    suspend fun updateSubscriptionTier(userId: String, tier: SubscriptionTier, timestamp: Long)

    @Query("SELECT * FROM subscriptions WHERE userId = :userId")
    suspend fun getSubscription(userId: String): Subscription?

    // Score Snapshots
    @Insert
    suspend fun insertSnapshot(snapshot: ScoreSnapshot)

    @Query("SELECT * FROM score_snapshots WHERE childId = :childId ORDER BY weekStart DESC")
    suspend fun getSnapshotsForChild(childId: String): List<ScoreSnapshot>
}
