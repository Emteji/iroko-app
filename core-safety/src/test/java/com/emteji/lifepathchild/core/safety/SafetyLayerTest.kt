package com.emteji.lifepathchild.core.safety

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SafetyLayerTest {

    private val safetyLayer = SafetyLayer()

    @Test
    fun `validateContent returns true for safe content`() {
        val safeContent = "I want to learn about dinos."
        assertTrue(safetyLayer.validateContent(safeContent))
    }

    @Test
    fun `validateContent returns false for forbidden keyword violence`() {
        val unsafeContent = "I like violence in movies."
        assertFalse(safetyLayer.validateContent(unsafeContent))
    }

    @Test
    fun `validateContent returns false for forbidden keyword kill`() {
        val unsafeContent = "kill the process" // Context unaware, but strictly blocked by rules
        assertFalse(safetyLayer.validateContent(unsafeContent))
    }
}
