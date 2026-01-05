package com.emteji.lifepathchild.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emteji.lifepathchild.core.data.model.ReminderEntity
import com.emteji.lifepathchild.core.data.model.SignalEntity
import com.emteji.lifepathchild.core.data.model.MicroTaskEntity
import com.emteji.lifepathchild.core.data.model.MicroTaskProgress
import com.emteji.lifepathchild.core.data.model.RewardEntity
import com.emteji.lifepathchild.core.data.model.ChildProfile
import com.emteji.lifepathchild.core.data.model.ParentProfile
import com.emteji.lifepathchild.core.data.model.ScoreSnapshot
import com.emteji.lifepathchild.core.data.model.Subscription

@Database(
    entities = [
        ReminderEntity::class, 
        SignalEntity::class, 
        MicroTaskEntity::class, 
        MicroTaskProgress::class,
        RewardEntity::class,
        ChildProfile::class,
        ParentProfile::class,
        ScoreSnapshot::class,
        Subscription::class
    ], 
    version = 7, 
    exportSchema = false
)
@androidx.room.TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun signalDao(): SignalDao
    abstract fun microTaskDao(): MicroTaskDao
    abstract fun rewardDao(): RewardDao
    abstract fun userDao(): UserDao
}
