package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.database.SignalDao
import com.emteji.lifepathchild.core.data.model.SignalEntity
import com.emteji.lifepathchild.core.data.model.SignalType
import javax.inject.Inject
import javax.inject.Singleton

interface SignalRepository {
    suspend fun logSignal(type: SignalType, target: String, value: Float, context: String? = null)
    suspend fun getSignalsForTarget(target: String): List<SignalEntity>
    suspend fun getSignalsByType(type: SignalType, since: Long): List<SignalEntity>
}

@Singleton
class SignalRepositoryImpl @Inject constructor(
    private val signalDao: SignalDao
) : SignalRepository {

    override suspend fun logSignal(type: SignalType, target: String, value: Float, context: String?) {
        val signal = SignalEntity(
            type = type,
            target = target,
            value = value,
            context = context
        )
        signalDao.insertSignal(signal)
    }

    override suspend fun getSignalsForTarget(target: String): List<SignalEntity> {
        return signalDao.getSignalsForTarget(target)
    }

    override suspend fun getSignalsByType(type: SignalType, since: Long): List<SignalEntity> {
        return signalDao.getSignalsByType(type, since)
    }
}
