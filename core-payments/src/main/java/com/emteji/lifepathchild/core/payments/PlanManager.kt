package com.emteji.lifepathchild.core.payments

interface PlanManager {
    suspend fun refreshPlan()
    fun hasAccess(featureKey: String): Boolean
    fun getIntLimit(featureKey: String): Int
    fun getCurrentPlanId(): String
}
