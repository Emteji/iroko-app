package com.emteji.lifepathchild.core.ai

import com.emteji.lifepathchild.core.safety.SafetyLayer
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class AIServiceTest {

    private lateinit var safetyLayer: SafetyLayer
    private lateinit var aiService: AIService

    @Before
    fun setup() {
        safetyLayer = mock(SafetyLayer::class.java)
        aiService = AIService(safetyLayer)
    }

    @Test
    fun `getSafeResponse returns failure when prompt is unsafe`() = runTest {
        `when`(safetyLayer.validateContent("unsafe input")).thenReturn(false)
        
        val result = aiService.getSafeResponse("unsafe input")
        assertTrue(result.isFailure)
    }

    @Test
    fun `getSafeResponse returns success when prompt and response are safe`() = runTest {
        `when`(safetyLayer.validateContent("safe input")).thenReturn(true)
        // Mocking internal behavior is hard without interface, assuming internal mockAIProvider response is safe
        `when`(safetyLayer.validateContent("This is a safe and friendly response from the AI.")).thenReturn(true)

        val result = aiService.getSafeResponse("safe input")
        assertTrue(result.isSuccess)
    }
}
