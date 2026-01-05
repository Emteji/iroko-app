package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.model.RiskLevel
import com.emteji.lifepathchild.core.data.model.SimulationOption
import com.emteji.lifepathchild.core.data.model.SimulationScenario
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimulationRepository @Inject constructor() {

    // Helper to get initial scenarios (Mock DB for now)
    fun getScenarios(): List<SimulationScenario> {
        return listOf(
            SimulationScenario(
                id = "sim_wallet",
                storyText = "You find a wallet on the playground. No one is looking. What do you do?",
                timeLimitSeconds = 15, // Time pressure
                relatedInterest = "SOCIAL",
                options = listOf(
                    SimulationOption(
                        id = "opt_return",
                        text = "Take it to the teacher immediately.",
                        riskLevel = RiskLevel.LOW,
                        isRuleBreak = false,
                        outcomeText = "Safe and responsible."
                    ),
                    SimulationOption(
                        id = "opt_check",
                        text = "Open it to see who it belongs to.",
                        riskLevel = RiskLevel.MODERATE,
                        isRuleBreak = false,
                        outcomeText = "Curious but risky."
                    ),
                    SimulationOption(
                        id = "opt_keep",
                        text = "Put it in your pocket for later.",
                        riskLevel = RiskLevel.HIGH,
                        isRuleBreak = true,
                        outcomeText = "Rule breaking behavior observed."
                    )
                )
            ),
            SimulationScenario(
                id = "sim_climb",
                storyText = "There is a high wall at the park. Your friends are climbing it. It says 'No Climbing'.",
                timeLimitSeconds = 10,
                relatedInterest = "LEADERSHIP",
                options = listOf(
                    SimulationOption(
                        id = "opt_stop",
                        text = "Tell them to stop because it's dangerous.",
                        riskLevel = RiskLevel.LOW,
                        isRuleBreak = false,
                        outcomeText = "Leader / Rule follower."
                    ),
                    SimulationOption(
                        id = "opt_join_careful",
                        text = "Climb only the lowest part carefully.",
                        riskLevel = RiskLevel.MODERATE,
                        isRuleBreak = true,
                        outcomeText = "Calculated risk."
                    ),
                    SimulationOption(
                        id = "opt_race",
                        text = "Race them to the top!",
                        riskLevel = RiskLevel.HIGH,
                        isRuleBreak = true,
                        outcomeText = "High risk / Impulsive."
                    )
                )
            )
        )
    }
}
