package com.emteji.lifepathchild.core.data.di

import android.content.Context
import androidx.room.Room
import com.emteji.lifepathchild.core.data.database.AppDatabase
import com.emteji.lifepathchild.core.data.database.ReminderDao
import com.emteji.lifepathchild.core.data.database.SignalDao
import com.emteji.lifepathchild.core.data.database.MicroTaskDao
import com.emteji.lifepathchild.core.data.database.RewardDao
import com.emteji.lifepathchild.core.data.database.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "lifepath-child.db"
        ).build()
    }

    @Provides
    fun provideReminderDao(database: AppDatabase): ReminderDao {
        return database.reminderDao()
    }

    @Provides
    fun provideSignalDao(database: AppDatabase): SignalDao {
        return database.signalDao()
    }

    @Provides
    fun provideMicroTaskDao(database: AppDatabase): MicroTaskDao {
        return database.microTaskDao()
    }

    @Provides
    fun provideRewardDao(database: AppDatabase): RewardDao {
        return database.rewardDao()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}
