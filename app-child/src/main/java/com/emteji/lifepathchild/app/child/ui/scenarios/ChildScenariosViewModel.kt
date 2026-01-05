package com.emteji.lifepathchild.app.child.ui.scenarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.data.model.Scenario
import com.emteji.lifepathchild.core.data.repository.ScenariosRepository
import com.emteji.lifepathchild.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildScenariosViewModel @Inject constructor(
    private val scenariosRepository: ScenariosRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScenariosUiState())
    val uiState: StateFlow<ScenariosUiState> = _uiState.asStateFlow()

    init {
        loadScenarios()
    }

    fun loadScenarios() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val childResult = userRepository.getCurrentChild()
            childResult.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Could not load profile.")
                return@launch
            }
            val child = childResult.getOrThrow()

            val result = scenariosRepository.getScenariosForChild(child.id)
            result.onSuccess { scenarios ->
                // Filter out scenarios that already have feedback (meaning they are done)
                val activeScenarios = scenarios.filter { it.outcomeFeedback == null }
                _uiState.value = _uiState.value.copy(isLoading = false, scenarios = activeScenarios)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = error.message)
            }
        }
    }

    fun submitChoice(scenarioId: String, choiceText: String) {
        viewModelScope.launch {
            // In a real app, this would likely trigger an AI evaluation or backend logic
            // For now, we just update the outcome feedback to mark it as 'done' locally or simple string
            val feedback = "You chose: $choiceText. Good choice!" 
            
            val result = scenariosRepository.submitChoice(scenarioId, feedback)
            if (result.isSuccess) {
                loadScenarios()
            }
        }
    }
}

data class ScenariosUiState(
    val isLoading: Boolean = false,
    val scenarios: List<Scenario> = emptyList(),
    val error: String? = null
)
