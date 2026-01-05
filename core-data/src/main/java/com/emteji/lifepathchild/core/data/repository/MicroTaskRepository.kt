package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.database.MicroTaskDao
import com.emteji.lifepathchild.core.data.model.MicroTaskEntity
import com.emteji.lifepathchild.core.data.model.MicroTaskProgress
import com.emteji.lifepathchild.core.data.model.TaskCategory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MicroTaskRepository @Inject constructor(
    private val microTaskDao: MicroTaskDao
) {

    suspend fun getNextTask(category: TaskCategory): MicroTaskEntity? {
        return microTaskDao.getNextIncompleteTask(category)
    }

    suspend fun getNextTask(level: Int): MicroTaskEntity? {
        return microTaskDao.getNextIncompleteTaskByLevel(level)
    }

    suspend fun completeTask(taskId: String, outcome: String) {
        val progress = MicroTaskProgress(
            taskId = taskId,
            isCompleted = true,
            outcome = outcome,
            completedAt = System.currentTimeMillis()
        )
        microTaskDao.updateProgress(progress)
    }

    suspend fun seedInitialTasks() {
        // Math Interest Test
        val mathTasks = listOf(
            MicroTaskEntity("math_l1", TaskCategory.MATH, 1, "Sort Objects", "Sort blocks by color or shape.", 0),
            MicroTaskEntity("math_l2", TaskCategory.MATH, 2, "Count Objects", "Count the sorted piles.", 1),
            MicroTaskEntity("math_l3", TaskCategory.MATH, 3, "Price Objects", "Assign 'prices' to items (1 coin, 2 coins).", 2)
        )

        // Social Interest Test
        val socialTasks = listOf(
            MicroTaskEntity("social_l1", TaskCategory.SOCIAL, 1, "Help Sibling", "Assist with a simple task.", 0),
            MicroTaskEntity("social_l2", TaskCategory.SOCIAL, 2, "Trade Roles", "Switch who is the 'leader' in a game.", 1),
            MicroTaskEntity("social_l3", TaskCategory.SOCIAL, 3, "Lead Group", "Explain rules to others.", 2)
        )

         // Leadership Interest Test
        val leadershipTasks = listOf(
            MicroTaskEntity("lead_l1", TaskCategory.LEADERSHIP, 1, "Decide Rules", "Pick one rule for the game.", 0),
            MicroTaskEntity("lead_l2", TaskCategory.LEADERSHIP, 2, "Explain Rules", "Tell the rule to someone else.", 1),
            MicroTaskEntity("lead_l3", TaskCategory.LEADERSHIP, 3, "Correct Calmly", "Spot a mistake and fix it gently.", 2)
        )

        // Part 4: Non Toy Methods (Life Tasks)
        val lifeTasks = listOf(
            MicroTaskEntity("life_market", TaskCategory.MATH, 1, "Market Visit", "Compare prices of two items.", 0, listOf("no-toy", "financial")),
            MicroTaskEntity("life_chore", TaskCategory.SOCIAL, 1, "House Help", "Assist with folding clothes or setting table.", 0, listOf("no-toy", "chore")),
            MicroTaskEntity("life_event", TaskCategory.LEADERSHIP, 2, "Plan Event", "Choose snacks for a small family movie night.", 0, listOf("no-toy", "event")),
            MicroTaskEntity("life_money", TaskCategory.MATH, 2, "Pocket Money", "Count coins and decide what to save.", 0, listOf("no-toy", "financial"))
        )

        (mathTasks + socialTasks + leadershipTasks + lifeTasks).forEach {
            microTaskDao.insertTask(it)
        }
    }
}
