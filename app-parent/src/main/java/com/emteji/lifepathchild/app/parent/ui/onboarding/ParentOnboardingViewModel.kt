package com.emteji.lifepathchild.app.parent.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentOnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            userRepository.setParentOnboardingCompleted(true)
        }
    }
}
