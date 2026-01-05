package com.emteji.lifepathchild.app.child.ui.dailypath

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.ai.AIService
import com.emteji.lifepathchild.core.ai.model.ChildInputProfile
import com.emteji.lifepathchild.core.data.model.Task
import com.emteji.lifepathchild.core.data.repository.TaskRepository
import com.emteji.lifepathchild.core.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DailyPathViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val aiService: AIService
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _childName = MutableStateFlow("Child")
    val childName: StateFlow<String> = _childName

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // In a real flow, we get the current logged-in child's ID (or locally saved one)
                val currentChildId = userRepository.getPairedChildId() ?: "mock-child-id"
                
                // Load Name
                val childResult = userRepository.getCurrentChild()
                val child = childResult.getOrNull()
                _childName.value = child?.name ?: "Child"
                
                val result = taskRepository.getTasksForChild(currentChildId)
                val existingTasks = result.getOrDefault(emptyList())
                
                if (existingTasks.isNotEmpty()) {
                    _tasks.value = existingTasks
                } else if (child != null) {
                    // GENERATE AI TASKS
                    val input = ChildInputProfile(coreData = child, recentActivity = emptyList())
                    val aiPlanResult = aiService.generateDailyPlan(input)
                    
                    aiPlanResult.onSuccess { planText ->
                        // Parse AI text into tasks (Simple Mock Parser)
                        // Assuming AI returns 3 lines of tasks
                        val newTasks = planText.lines()
                            .filter { it.isNotBlank() }
                            .take(3)
                            .mapIndexed { index, line ->
                                Task(
                                    id = UUID.randomUUID().toString(),
                                    childId = currentChildId,
                                    title = line.trim().removePrefix("- ").removePrefix("1. "),
                                    rewardPoints = 10 + (index * 5),
                                    aiGenerated = true,
                                    difficulty = if (index == 2) "Hard" else "Medium"
                                )
                            }
                        
                        _tasks.value = newTasks
                        // Save to repo in background
                        newTasks.forEach { taskRepository.createTask(it) }
                    }
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
