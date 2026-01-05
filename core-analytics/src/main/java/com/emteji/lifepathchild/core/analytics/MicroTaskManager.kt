package com.emteji.lifepathchild.core.analytics

import com.emteji.lifepathchild.core.data.model.MicroTaskEntity
import com.emteji.lifepathchild.core.data.model.SignalType
import com.emteji.lifepathchild.core.data.model.TaskCategory
import com.emteji.lifepathchild.core.data.repository.MicroTaskRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MicroTaskManager @Inject constructor(
    private val microTaskRepository: MicroTaskRepository,
    private val interestEngine: InterestEngine
) {

    /**
     * Retrieves the next appropriate task for the child in a specific category.
     * Enforces strict progression: Level 1 must be done before Level 2.
     */
    suspend fun getNextMission(category: TaskCategory): MicroTaskEntity? {
        return microTaskRepository.getNextTask(category)
    }

    suspend fun getNextTask(level: Int): MicroTaskEntity? {
        return microTaskRepository.getNextTask(level)
    }

    /**
     * Marks a mission as complete and feeds the user's reaction/performance 
     * into the Interest Engine.
     * 
     * @param taskId The ID of the completed task.
     * @param category The category of the task.
     * @param outcome A description of the reaction (e.g., "Enthusiastic", "Struggled").
     * @param effortScore A 1-10 rating of effort observed.
     */
    suspend fun completeMission(taskId: String, category: TaskCategory, outcome: String, effortScore: Float) {
        // 1. Mark as complete in DB ensures next level unlocks
        microTaskRepository.completeTask(taskId, outcome)

        // 2. Feed signals to Interest Engine
        // Base CHOice signal implies they engaged in the task
        interestEngine.submitSignal(
            type = SignalType.CHOICE,
            target = category.name,
            value = 1.0f,
            context = "Mission Completed: $taskId"
        )

        // Effort signal
        interestEngine.submitSignal(
            type = SignalType.EFFORT,
            target = category.name,
            value = effortScore,
            context = "Mission Effort: $outcome"
        )
        
        // If outcome suggests high interest, maybe boost further
        if (outcome.contains("Enthusiastic", ignoreCase = true)) {
             interestEngine.submitSignal(
                type = SignalType.EMOTION,
                target = category.name,
                value = 1.0f, // Max emotion score
                context = "Enthusiastic Reaction"
            )
        }
    }
    
    // Helper to seed logic
    suspend fun initialize() {
        microTaskRepository.seedInitialTasks()
    }
}
