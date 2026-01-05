package com.emteji.lifepathchild.core.data.model

data class SimulationScenario(
    val id: String,
    val storyText: String,
    val options: List<SimulationOption>,
    val timeLimitSeconds: Int = 10,
    val relatedInterest: String // e.g., "LEADERSHIP", "SOCIAL"
)

data class SimulationOption(
    val id: String,
    val text: String,
    val riskLevel: RiskLevel = RiskLevel.LOW,
    val isRuleBreak: Boolean = false,
    val outcomeText: String? = null
)

enum class RiskLevel {
    LOW,
    MODERATE,
    HIGH
}
