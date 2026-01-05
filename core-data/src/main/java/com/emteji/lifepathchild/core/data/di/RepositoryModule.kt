package com.emteji.lifepathchild.core.data.di

import com.emteji.lifepathchild.core.data.repository.ReminderRepository
import com.emteji.lifepathchild.core.data.repository.ReminderRepositoryImpl
import com.emteji.lifepathchild.core.data.repository.SignalRepository
import com.emteji.lifepathchild.core.data.repository.SignalRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindReminderRepository(
        reminderRepositoryImpl: ReminderRepositoryImpl
    ): ReminderRepository

    @Binds
    abstract fun bindSignalRepository(
        signalRepositoryImpl: SignalRepositoryImpl
    ): SignalRepository
}
