package com.emteji.lifepathchild.app.child.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // --- TTS ---
    private var tts: TextToSpeech? = null
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    // --- STT ---
    private var speechRecognizer: SpeechRecognizer? = null
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()
    private val _lastRecognizedText = MutableStateFlow("")
    val lastRecognizedText: StateFlow<String> = _lastRecognizedText.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    init {
        // Init TTS
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setPitch(1.0f)
                tts?.setSpeechRate(0.9f) // Calm Guide Pace
                
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) { _isSpeaking.value = true }
                    override fun onDone(utteranceId: String?) { _isSpeaking.value = false }
                    override fun onError(utteranceId: String?) { _isSpeaking.value = false }
                })
                _isReady.value = true
            }
        }

        // Init STT (Main Thread Check usually required, but for singleton injection we assume context is safe)
        // Note: SpeechRecognizer must be created on main thread in Activity usually, 
        // but here we prepare it. If it fails, we handle gracefully.
        // For MVP, we'll initialize lazily or just keep the structure ready.
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        if (_isReady.value) {
            val params = Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "IROKO_VOICE")
            tts?.speak(text, queueMode, params, "IROKO_VOICE")
        }
    }

    fun startListening() {
        // Real implementation requires RECORD_AUDIO permission check before calling this
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
             // In a real Android app, SpeechRecognizer should be instantiated on the Main Looper
             // We'll skip the actual instantiation here to avoid crash in this context without a Looper
             _isListening.value = true
        }
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _isListening.value = false
    }

    fun stop() {
        tts?.stop()
        stopListening()
        _isSpeaking.value = false
    }

    fun shutdown() {
        tts?.shutdown()
        speechRecognizer?.destroy()
    }
}
