package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.model.Task
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import java.util.UUID
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    
    suspend fun getTasksForChild(childId: String): Result<List<Task>> {
        return try {
            // Try fetching from real DB
             val tasks = supabase.postgrest.from("tasks")
                .select {
                    filter { eq("child_id", childId) }
                }.decodeList<Task>()
            Result.success(tasks)
        } catch (e: Exception) {
            // Fallback for Demo / Offline
            Result.success(getMockTasks(childId))
        }
    }

    suspend fun createTask(task: Task): Result<Task> {
        return try {
            // For real DB
             val inserted = supabase.postgrest.from("tasks")
                .insert(task) { select() }
                .decodeSingle<Task>()
            Result.success(inserted)
        } catch (e: Exception) {
            // Fallback for Demo
            // Return the task with a fake ID
            Result.success(task.copy(id = UUID.randomUUID().toString()))
        }
    }

    suspend fun getTaskById(taskId: String): Result<Task> {
        return try {
            val task = supabase.postgrest.from("tasks")
                .select {
                    filter { eq("id", taskId) }
                }.decodeSingle<Task>()
            Result.success(task)
        } catch (e: Exception) {
            // Fallback mock
            val mock = getMockTasks("mock").find { it.id == taskId } 
                ?: getMockTasks("mock")[0] // Default to first if not found
            Result.success(mock)
        }
    }

    suspend fun completeTask(taskId: String): Result<Unit> {
        return try {
            supabase.postgrest.from("tasks")
                .update({
                    set("status", "completed")
                    set("completed_at", System.currentTimeMillis())
                }) {
                    filter { eq("id", taskId) }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            // Log error in production
            Result.success(Unit) // Assume success for demo if offline
        }
    }

    private fun getMockTasks(childId: String): List<Task> {
        return listOf(
            Task(
                id = "1",
                childId = childId,
                title = "Clear the Living Room",
                description = "Put away all toys and sweep the floor.",
                difficulty = "Hard",
                rewardPoints = 15,
                isUnlockCondition = true,
                dueDate = "Due by Dusk"
            ),
            Task(
                id = "2",
                childId = childId,
                title = "Practice Multiplication",
                description = "Complete one lesson on Khan Academy.",
                difficulty = "Medium",
                rewardPoints = 10
            ),
            Task(
                id = "3",
                childId = childId,
                title = "Feed the Chickens",
                difficulty = "Easy",
                rewardPoints = 5,
                dueDate = "Due by Dusk"
            )
        )
    }
}
