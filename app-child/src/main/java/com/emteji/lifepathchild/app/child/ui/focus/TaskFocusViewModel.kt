package com.emteji.lifepathchild.app.child.ui.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.app.child.voice.VoiceEngine
import com.emteji.lifepathchild.core.data.model.Task
import com.emteji.lifepathchild.core.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskFocusViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val voiceEngine: VoiceEngine
) : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    val isSpeaking: StateFlow<Boolean> = voiceEngine.isSpeaking

    fun loadTask(taskId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = taskRepository.getTaskById(taskId)
            _task.value = result.getOrNull()
            _isLoading.value = false
            
            // Speak Coach Intro
            result.getOrNull()?.let {
                voiceEngine.speak("Your mission is to ${it.title}. Focus on doing it well.")
            }
        }
    }

    fun completeTask(onDone: () -> Unit) {
        viewModelScope.launch {
            voiceEngine.speak("Well done. One step closer to mastery.")
            
            // Sync to Backend
            _task.value?.id?.let { id ->
                taskRepository.completeTask(id)
            }
            
            // Delay to let voice finish
            kotlinx.coroutines.delay(2000)
            onDone()
        }
    }
}
