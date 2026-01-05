package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.database.ReminderDao
import com.emteji.lifepathchild.core.data.model.ReminderEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface ReminderRepository {
    fun getActiveReminders(): Flow<List<ReminderEntity>>
    suspend fun getReminder(id: Long): ReminderEntity?
    suspend fun scheduleReminder(reminder: ReminderEntity): Long
    suspend fun updateReminder(reminder: ReminderEntity)
    suspend fun cancelReminder(reminder: ReminderEntity)
}

@Singleton
class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {
    override fun getActiveReminders(): Flow<List<ReminderEntity>> = reminderDao.getActiveReminders()

    override suspend fun getReminder(id: Long): ReminderEntity? = reminderDao.getReminder(id)

    override suspend fun scheduleReminder(reminder: ReminderEntity): Long = reminderDao.insertReminder(reminder)

    override suspend fun updateReminder(reminder: ReminderEntity) = reminderDao.updateReminder(reminder)

    override suspend fun cancelReminder(reminder: ReminderEntity) = reminderDao.deleteReminder(reminder)
}
