package com.emteji.lifepathchild.app.parent.ui.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.data.model.Child
import com.emteji.lifepathchild.core.data.model.DevicePolicy
import com.emteji.lifepathchild.core.data.repository.DeviceControlRepository
import com.emteji.lifepathchild.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceControlViewModel @Inject constructor(
    private val deviceControlRepository: DeviceControlRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _selectedChild = MutableStateFlow<Child?>(null)
    val selectedChild: StateFlow<Child?> = _selectedChild.asStateFlow()
    
    private val _policy = MutableStateFlow<DevicePolicy?>(null)
    val policy: StateFlow<DevicePolicy?> = _policy.asStateFlow()

    private val _children = MutableStateFlow<List<Child>>(emptyList())
    val children: StateFlow<List<Child>> = _children.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val childrenResult = userRepository.getChildrenForParent()
            val list = childrenResult.getOrDefault(emptyList())
            _children.value = list
            
            if (list.isNotEmpty()) {
                selectChild(list[0])
            }
        }
    }

    fun selectChild(child: Child) {
        _selectedChild.value = child
        loadPolicy(child.id)
    }

    private fun loadPolicy(childId: String) {
        viewModelScope.launch {
            val result = deviceControlRepository.getPolicyForChild(childId)
            _policy.value = result.getOrNull()
        }
    }

    fun updateScreenTimeLimit(minutes: Int) {
        val current = _policy.value ?: return
        val newPolicy = current.copy(dailyLimitMinutes = minutes)
        _policy.value = newPolicy
        savePolicy(newPolicy)
    }

    fun toggleLock(locked: Boolean) {
        val current = _policy.value ?: return
        val newPolicy = current.copy(isLocked = locked)
        _policy.value = newPolicy
        savePolicy(newPolicy)
    }

    private fun savePolicy(policy: DevicePolicy) {
        viewModelScope.launch {
            deviceControlRepository.updatePolicy(policy)
        }
    }
}
