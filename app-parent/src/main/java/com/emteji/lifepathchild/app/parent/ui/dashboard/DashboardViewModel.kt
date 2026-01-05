package com.emteji.lifepathchild.app.parent.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.data.model.ChildProfile
import com.emteji.lifepathchild.core.data.model.MoodEntry
import com.emteji.lifepathchild.core.data.repository.ChildRepository
import com.emteji.lifepathchild.core.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val childRepository: com.emteji.lifepathchild.core.data.repository.ChildRepository,
    private val moodRepository: com.emteji.lifepathchild.core.data.repository.MoodRepository
) : ViewModel() {

    private val _children = MutableStateFlow<List<ChildProfile>>(emptyList())
    val children: StateFlow<List<ChildProfile>> = _children.asStateFlow()

    private val _selectedChild = MutableStateFlow<ChildProfile?>(null)
    val selectedChild: StateFlow<ChildProfile?> = _selectedChild.asStateFlow()
    
    private val _recentMoods = MutableStateFlow<List<MoodEntry>>(emptyList())
    val recentMoods: StateFlow<List<MoodEntry>> = _recentMoods.asStateFlow()

    init {
        loadChildren()
    }

    private fun loadChildren() {
        viewModelScope.launch {
            val result = childRepository.getChildrenForParent("parent-1") // Mock ID
            if (result.isSuccess) {
                val childList = result.getOrDefault(emptyList())
                _children.value = childList
                if (childList.isNotEmpty()) {
                    selectChild(childList[0])
                }
            }
        }
    }

    fun selectChild(child: ChildProfile) {
        _selectedChild.value = child
        loadMoods(child.id)
    }
    
    private fun loadMoods(childId: String) {
        viewModelScope.launch {
            val result = moodRepository.getRecentMoods(childId)
            _recentMoods.value = result.getOrDefault(emptyList())
        }
    }
}
