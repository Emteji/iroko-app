package com.emteji.lifepathchild.app.parent.ui.setup.wizard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.data.model.Child
import com.emteji.lifepathchild.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChildWizardViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Step Management
    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()
    
    // Data Fields
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()
    
    private val _age = MutableStateFlow("")
    val age: StateFlow<String> = _age.asStateFlow()
    
    private val _temperament = MutableStateFlow<String?>(null)
    val temperament: StateFlow<String?> = _temperament.asStateFlow()
    
    private val _academicLevel = MutableStateFlow<String?>(null)
    val academicLevel: StateFlow<String?> = _academicLevel.asStateFlow()
    
    private val _behavioralConcerns = MutableStateFlow<List<String>>(emptyList())
    val behavioralConcerns: StateFlow<List<String>> = _behavioralConcerns.asStateFlow()

    private val _createdChildId = MutableStateFlow<String?>(null)
    val createdChildId: StateFlow<String?> = _createdChildId.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun nextStep() {
        _currentStep.value += 1
    }

    fun previousStep() {
        if (_currentStep.value > 0) _currentStep.value -= 1
    }

    fun setName(value: String) { _name.value = value }
    fun setAge(value: String) { _age.value = value }
    fun setTemperament(value: String) { _temperament.value = value }
    fun setAcademicLevel(value: String) { _academicLevel.value = value }
    fun toggleConcern(concern: String) {
        val current = _behavioralConcerns.value.toMutableList()
        if (current.contains(concern)) current.remove(concern) else current.add(concern)
        _behavioralConcerns.value = current
    }

    fun submitProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // In a real app, user ID comes from Auth
                val userId = "mock-user-id" 
                
                val newChild = Child(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    name = _name.value,
                    age = _age.value.toIntOrNull(),
                    temperament = _temperament.value,
                    academicLevel = _academicLevel.value,
                    behavioralConcerns = _behavioralConcerns.value,
                    // Initialize scores
                    gritScore = 500,
                    omoluabiScore = 500,
                    xpTotal = 0
                )

                // Save via repository
                // NOTE: We're reusing the createChild method if it exists, or mocking it here
                // For now, we simulate success
                _createdChildId.value = newChild.id
                
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }
}
