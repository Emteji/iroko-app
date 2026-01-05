package com.emteji.lifepathchild.app.child.ui.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.data.model.Mission
import com.emteji.lifepathchild.core.data.model.MissionStatus
import com.emteji.lifepathchild.core.data.repository.MissionsRepository
import com.emteji.lifepathchild.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildMissionsViewModel @Inject constructor(
    private val missionsRepository: MissionsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChildMissionsUiState())
    val uiState: StateFlow<ChildMissionsUiState> = _uiState.asStateFlow()

    init {
        loadMissions()
    }

    fun loadMissions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // 1. Get Child ID
            val childResult = userRepository.getCurrentChild()
            childResult.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Could not load profile. Are you logged in?")
                return@launch
            }
            val child = childResult.getOrThrow()

            // 2. Get Missions
            val missionsResult = missionsRepository.getMissionsForChild(child.id)
            missionsResult.onSuccess { missions ->
                 _uiState.value = _uiState.value.copy(isLoading = false, missions = missions)
            }.onFailure { error ->
                 _uiState.value = _uiState.value.copy(isLoading = false, error = error.message)
            }
        }
    }

    fun completeMission(missionId: String) {
        viewModelScope.launch {
            // In real app, upload proof first. Here we skip proof upload or use placeholder.
            val result = missionsRepository.updateStatus(missionId, MissionStatus.COMPLETED, "https://placeholder.com/proof.jpg")
            if (result.isSuccess) {
                loadMissions()
            }
        }
    }
}

data class ChildMissionsUiState(
    val isLoading: Boolean = false,
    val missions: List<Mission> = emptyList(),
    val error: String? = null
)
