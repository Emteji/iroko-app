package com.emteji.lifepathchild.core.safety

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddictionDetector @Inject constructor() {

    private val usageLogs = mutableListOf<Long>()

    fun logUsage() {
        usageLogs.add(System.currentTimeMillis())
    }

    fun detectAddictionPattern(): Boolean {
        // Simple mock logic: If used more than 5 times in last minute
        val now = System.currentTimeMillis()
        val recentUsage = usageLogs.filter { now - it < 60000 }
        return recentUsage.size > 5
    }

    fun getSafetyAlert(): String? {
        if (detectAddictionPattern()) {
            return "Usage limit exceeded! Take a break."
        }
        return null
    }
}
