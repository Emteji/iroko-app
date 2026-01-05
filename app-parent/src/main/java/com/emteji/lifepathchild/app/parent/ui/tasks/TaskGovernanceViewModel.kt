package com.emteji.lifepathchild.app.parent.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.data.model.Child
import com.emteji.lifepathchild.core.data.model.Task
import com.emteji.lifepathchild.core.data.repository.TaskRepository
import com.emteji.lifepathchild.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TaskGovernanceViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _children = MutableStateFlow<List<Child>>(emptyList())
    val children: StateFlow<List<Child>> = _children.asStateFlow()
    
    private val _selectedChild = MutableStateFlow<Child?>(null)
    val selectedChild: StateFlow<Child?> = _selectedChild.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // 1. Load Children
            val childrenResult = userRepository.getChildrenForParent()
            val childList = childrenResult.getOrDefault(emptyList())
            _children.value = childList
            
            // 2. Select first child if available
            if (childList.isNotEmpty() && _selectedChild.value == null) {
                _selectedChild.value = childList[0]
            }
            
            // 3. Load tasks for selected child
            _selectedChild.value?.let { child ->
                loadTasks(child.id)
            }
            
            _isLoading.value = false
        }
    }

    fun selectChild(child: Child) {
        _selectedChild.value = child
        loadTasks(child.id)
    }

    private fun loadTasks(childId: String) {
        viewModelScope.launch {
            val result = taskRepository.getTasksForChild(childId)
            _tasks.value = result.getOrDefault(emptyList())
        }
    }

    fun createTask(
        title: String,
        points: Int,
        difficulty: String,
        isUnlock: Boolean,
        deadline: String,
        onSuccess: () -> Unit
    ) {
        val child = _selectedChild.value ?: return
        
        viewModelScope.launch {
            _isLoading.value = true
            val newTask = Task(
                id = UUID.randomUUID().toString(), // Temp ID, repo might replace
                childId = child.id,
                title = title,
                rewardPoints = points,
                difficulty = difficulty,
                isUnlockCondition = isUnlock,
                dueDate = deadline
            )
            
            val result = taskRepository.createTask(newTask)
            if (result.isSuccess) {
                loadTasks(child.id) // Refresh
                onSuccess()
            }
            _isLoading.value = false
        }
    }
}
