package com.emteji.lifepathchild.app.child.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildLoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _pairingCode = MutableStateFlow("")
    val pairingCode: StateFlow<String> = _pairingCode.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun updateCode(code: String) {
        _pairingCode.value = code
        _error.value = null
    }

    fun attemptPairing(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (_pairingCode.value.length < 8) {
                _error.value = "Code must be at least 8 characters"
                return@launch
            }

            _isLoading.value = true
            
            try {
                // In a real app, we'd query Supabase: SELECT * FROM children WHERE id LIKE 'code%'
                // For MVP/Demo without RLS blocking us:
                // We'll treat the entered code as the Child ID (if it's a full UUID) OR 
                // just assume success if it looks like a partial UUID from the parent app.
                
                // If the user entered the 8-char code from Parent App, we can't easily find the UUID without a query.
                // However, since we might be offline or using mock data in this turn:
                
                // 1. Try to find child by this partial ID (Repository call)
                val result = userRepository.findChildByPairingCode(_pairingCode.value)
                
                if (result.isSuccess) {
                    val child = result.getOrNull()
                    if (child != null) {
                        // 2. Save Child ID locally
                        userRepository.savePairedChildId(child.id)
                        onSuccess()
                    } else {
                         _error.value = "Invalid Pairing Code"
                    }
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Pairing Failed"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
