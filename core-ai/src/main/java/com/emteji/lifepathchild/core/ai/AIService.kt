package com.emteji.lifepathchild.core.ai

import com.emteji.lifepathchild.core.ai.model.ChildInputProfile
import com.emteji.lifepathchild.core.safety.SafetyLayer
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.functions.functions
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class AIRequest(
    val prompt: String
)

@Serializable
data class AIResponse(
    val text: String
)

@Singleton
class AIService @Inject constructor(
    private val safetyLayer: SafetyLayer,
    private val backendCore: com.emteji.lifepathchild.core.data.repository.BackendCoreRepository
) {
    // SECTION 6: Parent-Facing Generations
    suspend fun generateDailyPlan(input: ChildInputProfile): Result<String> {
        val prompt = PromptTemplates.dailyPlanningPrompt(input)
        return getSafeResponse(prompt)
    }

    suspend fun generateBehaviorAnalysis(input: ChildInputProfile): Result<String> {
        val prompt = PromptTemplates.behaviorAnalysisPrompt(input)
        return getSafeResponse(prompt)
    }

    // SECTION 7: Child-Facing Generations
    suspend fun generateMission(input: ChildInputProfile): Result<String> {
        val prompt = PromptTemplates.missionGenerationPrompt(input)
        return getSafeResponse(prompt)
    }

    suspend fun generateEncouragement(completedTask: String): Result<String> {
        val prompt = PromptTemplates.encouragementPrompt(completedTask)
        return getSafeResponse(prompt)
    }

    suspend fun getSafeResponse(prompt: String): Result<String> {
        // 1. Validate Input
        if (!safetyLayer.validateContent(prompt)) {
            return Result.failure(Exception("Unsafe content detected in prompt."))
        }

        // 2. Call Backend Proxy
        return backendCore.generateAIResponse(prompt).fold(
            onSuccess = { responseText ->
                 // 3. Validate Output
                if (!safetyLayer.validateContent(responseText)) {
                    Result.failure(Exception("Unsafe content detected in response."))
                } else {
                    Result.success(responseText)
                }
            },
            onFailure = { e ->
                // Fallback for offline or error
                e.printStackTrace()
                Result.success("AI Service (Offline/Fallback): Unable to reach cloud. Suggestions: 1. Read a book. 2. Clean room. 3. Help cook.")
            }
        )
    }

    suspend fun getPersonaResponse(prompt: String, persona: Persona): Result<String> {
        // 1. Validate Input
        if (!safetyLayer.validateContent(prompt)) {
            return Result.failure(Exception("Unsafe content detected in prompt."))
        }

        // 2. Construct Safe Prompt with Persona
        val safePrompt = "${AIGovernance.SAFETY_PROMPT_PREFIX}\n\nSYSTEM: ${persona.promptContext}\n\nUser: $prompt"

        // 3. Call Backend Proxy
        return getSafeResponse(safePrompt)
    }
}
