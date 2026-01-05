package com.emteji.lifepathchild.core.payments

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscriptionManager @Inject constructor() {

    private val _currentPlan = MutableStateFlow<PlanType>(PlanType.FREE)
    val currentPlan: StateFlow<PlanType> = _currentPlan.asStateFlow()

    fun setPlan(plan: PlanType) {
        _currentPlan.value = plan
    }

    fun hasFeatureAccess(featureKey: String): Boolean {
        // Mock logic matching seed_plans.sql
        return when (featureKey) {
            FeatureKeys.AI_COACHING -> _currentPlan.value != PlanType.FREE
            FeatureKeys.DETAILED_REPORTS -> _currentPlan.value == PlanType.PREMIUM
            else -> true
        }
    }

    fun getMaxChildren(): Int {
        return when (_currentPlan.value) {
            PlanType.FREE -> 1
            PlanType.STANDARD -> 3
            PlanType.PREMIUM -> 999
        }
    }
}
