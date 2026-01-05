package com.emteji.lifepathchild.app.child.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.app.child.voice.VoiceEngine
import com.emteji.lifepathchild.core.data.model.MoodEntry
import com.emteji.lifepathchild.core.data.repository.MoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildDashboardViewModel @Inject constructor(
    private val voiceEngine: VoiceEngine,
    private val moodRepository: MoodRepository
) : ViewModel() {

    val isSpeaking: StateFlow<Boolean> = voiceEngine.isSpeaking

    init {
        // Welcome the child on launch
        viewModelScope.launch {
            // Delay slightly to let UI settle
            kotlinx.coroutines.delay(1000)
            voiceEngine.speak("Welcome back to your village, Joshua. I have prepared your path for today.")
        }
    }
    
    fun saveMood(label: String) {
        viewModelScope.launch {
            // "123" is hardcoded childId for MVP. In real app, get from UserSession
            moodRepository.saveMood(MoodEntry(childId = "123", moodLabel = label))
            
            // Ask Backend AI for response
            try {
                // TODO: Replace with real Retrofit call once implemented
                // val response = api.chat(message = "I am feeling $label", childName = "Joshua")
                // voiceEngine.speak(response.text)
                
                // Fallback for now until Retrofit is wired:
                 val response = when(label) {
                    "Happy", "Proud" -> "I am glad you feel strong. Let us use that energy."
                    "Sad", "Tired" -> "It is okay to rest. Trees grow slowly. Take a breath."
                    "Angry" -> "Fire can burn, or it can forge. Let us cool down together."
                    else -> "Thank you for sharing."
                }
                voiceEngine.speak(response)
            } catch (e: Exception) {
                voiceEngine.speak("I hear you.")
            }
        }
    }
}
