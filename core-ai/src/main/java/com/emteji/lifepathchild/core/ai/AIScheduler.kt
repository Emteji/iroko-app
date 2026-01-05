package com.emteji.lifepathchild.core.ai

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

interface AIScheduler {
    suspend fun suggestOptimalTime(currentSchedule: Long): Long
}

@Singleton
class AISchedulerStub @Inject constructor() : AIScheduler {
    override suspend fun suggestOptimalTime(currentSchedule: Long): Long {
        // Simulate AI analysis delay
        delay(500)
        // Suggest a time +/- 15 minutes based on "behavior analysis"
        val offset = Random.nextLong(-15 * 60 * 1000, 15 * 60 * 1000)
        return currentSchedule + offset
    }
}
