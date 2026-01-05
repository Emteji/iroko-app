package com.emteji.lifepathchild.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emteji.lifepathchild.core.data.model.SignalEntity
import com.emteji.lifepathchild.core.data.model.SignalType

@Dao
interface SignalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSignal(signal: SignalEntity): Long

    @Query("SELECT * FROM signals WHERE target = :target ORDER BY timestamp DESC")
    suspend fun getSignalsForTarget(target: String): List<SignalEntity>

    @Query("SELECT * FROM signals WHERE type = :type AND timestamp >= :since")
    suspend fun getSignalsByType(type: SignalType, since: Long): List<SignalEntity>

    @Query("SELECT * FROM signals WHERE timestamp >= :since")
    suspend fun getAllSignalsRecent(since: Long): List<SignalEntity>
}
