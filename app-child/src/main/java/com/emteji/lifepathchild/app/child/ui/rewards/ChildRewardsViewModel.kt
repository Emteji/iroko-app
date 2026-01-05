package com.emteji.lifepathchild.app.child.ui.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.data.model.Reward
import com.emteji.lifepathchild.core.data.repository.RewardsRepository
import com.emteji.lifepathchild.core.data.repository.UserRepository
import com.emteji.lifepathchild.core.data.repo.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildRewardsViewModel @Inject constructor(
    private val rewardsRepository: RewardsRepository,
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RewardsUiState())
    val uiState: StateFlow<RewardsUiState> = _uiState.asStateFlow()

    init {
        loadRewards()
    }

    fun loadRewards() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val childResult = userRepository.getCurrentChild()
            childResult.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Could not load profile.")
                return@launch
            }
            val child = childResult.getOrThrow()

            // Update current XP
            _uiState.value = _uiState.value.copy(currentXp = child.xpTotal)

            val result = rewardsRepository.getAvailableRewards(child.id)
            result.onSuccess { rewards ->
                // Filter unredeemed rewards
                val available = rewards.filter { !it.isRedeemed }
                _uiState.value = _uiState.value.copy(isLoading = false, rewards = available)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = error.message)
            }
        }
    }

    fun redeemReward(reward: Reward) {
        viewModelScope.launch {
            if (_uiState.value.currentXp < reward.xpCost) {
                _uiState.value = _uiState.value.copy(error = "Not enough XP!")
                return@launch
            }

            val childResult = userRepository.getCurrentChild()
            if (childResult.isFailure) {
                _uiState.value = _uiState.value.copy(error = "Profile unavailable.")
                return@launch
            }
            val child = childResult.getOrThrow()
            val wallet = walletRepository.walletForChild(child.id)
            if (wallet == null) {
                _uiState.value = _uiState.value.copy(error = "No wallet found.")
                return@launch
            }
            val result = rewardsRepository.requestSpend(wallet.id, child.id, reward.parentId, reward.xpCost)
            if (result.isSuccess) {
                loadRewards()
            } else {
                _uiState.value = _uiState.value.copy(error = "Redemption failed.")
            }
        }
    }
}

data class RewardsUiState(
    val isLoading: Boolean = false,
    val currentXp: Int = 0,
    val rewards: List<Reward> = emptyList(),
    val error: String? = null
)
