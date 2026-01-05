package com.emteji.lifepathchild.app.child.ui.simulation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.analytics.InterestEngine
import com.emteji.lifepathchild.core.data.model.RiskLevel
import com.emteji.lifepathchild.core.data.model.SignalType
import com.emteji.lifepathchild.core.data.model.SimulationOption
import com.emteji.lifepathchild.core.data.model.SimulationScenario
import com.emteji.lifepathchild.core.data.repository.SimulationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimulationViewModel @Inject constructor(
    private val repository: SimulationRepository,
    private val interestEngine: InterestEngine
) : ViewModel() {

    private val _currentScenario = MutableStateFlow<SimulationScenario?>(null)
    val currentScenario: StateFlow<SimulationScenario?> = _currentScenario.asStateFlow()

    private val _timeRemaining = MutableStateFlow(0)
    val timeRemaining: StateFlow<Int> = _timeRemaining.asStateFlow()
    
    // For feedback UI
    private val _outcome = MutableStateFlow<String?>(null)
    val outcome: StateFlow<String?> = _outcome.asStateFlow()

    private var timerJob: Job? = null
    private var startTime: Long = 0

    init {
        loadScenario()
        startTimer()
    }

    private fun loadScenario() {
        // Just load the first one for demo
        val scenarios = repository.getScenarios()
        _currentScenario.value = scenarios.firstOrNull()
        _timeRemaining.value = scenarios.firstOrNull()?.timeLimitSeconds ?: 10
        startTime = System.currentTimeMillis()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeRemaining.value > 0 && _outcome.value == null) {
                delay(1000)
                _timeRemaining.value -= 1
            }
            if (_outcome.value == null) {
                // Timeout logic
                handleTimeout()
            }
        }
    }

    fun onOptionSelected(option: SimulationOption) {
        timerJob?.cancel()
        val decisionTimeMs = System.currentTimeMillis() - startTime
        val scenario = _currentScenario.value ?: return

        _outcome.value = option.outcomeText

        // Submit Signals
        viewModelScope.launch {
            // 1. RISK Signal
            val riskValue = when (option.riskLevel) {
                RiskLevel.LOW -> 0.2f
                RiskLevel.MODERATE -> 0.5f
                RiskLevel.HIGH -> 0.9f
            }
            interestEngine.submitSignal(
                type = SignalType.TEMPERAMENT,
                target = "Risk Tolerance",
                value = riskValue,
                context = "Scenario: ${scenario.id}, Option: ${option.riskLevel}"
            )

            // 2. SPEED Signal (Faster = Higher Impulse/Confidence)
            // If very fast (< 3s) -> Impulsive?
            val speedFactor = if (decisionTimeMs < 3000) 1.0f else 0.5f
            interestEngine.submitSignal(
                type = SignalType.TEMPERAMENT,
                target = "Decision Speed",
                value = speedFactor,
                context = "Time: ${decisionTimeMs}ms"
            )
        }
    }

    private fun handleTimeout() {
        _outcome.value = "Time's up! Indecision observed."
        viewModelScope.launch {
             interestEngine.submitSignal(
                type = SignalType.TEMPERAMENT,
                target = "Indecision",
                value = 1.0f,
                context = "Timeout on Scenario"
            )
        }
    }
}
