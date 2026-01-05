package com.emteji.lifepathchild.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.emteji.lifepathchild.core.data.model.MicroTaskEntity
import com.emteji.lifepathchild.core.data.model.MicroTaskProgress
import com.emteji.lifepathchild.core.data.model.TaskCategory

@Dao
interface MicroTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: MicroTaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProgress(progress: MicroTaskProgress)

    @Query("SELECT * FROM micro_tasks WHERE category = :category AND level = :level")
    suspend fun getTasks(category: TaskCategory, level: Int): List<MicroTaskEntity>

    @Query("SELECT * FROM micro_task_progress WHERE taskId = :taskId")
    suspend fun getProgress(taskId: String): MicroTaskProgress?

    // Helper to get next available task (simplified logic for DAO, mostly handled in Manager)
    @Query("""
        SELECT * FROM micro_tasks 
        WHERE category = :category 
        AND id NOT IN (SELECT taskId FROM micro_task_progress WHERE isCompleted = 1)
        ORDER BY level ASC
        LIMIT 1
    """)
    suspend fun getNextIncompleteTask(category: TaskCategory): MicroTaskEntity?

    @Query("""
        SELECT * FROM micro_tasks 
        WHERE level = :level
        AND id NOT IN (SELECT taskId FROM micro_task_progress WHERE isCompleted = 1)
        LIMIT 1
    """)
    suspend fun getNextIncompleteTaskByLevel(level: Int): MicroTaskEntity?

    @Query("SELECT * FROM micro_tasks WHERE tags LIKE '%' || :tag || '%'")
    suspend fun getTasksByTag(tag: String): List<MicroTaskEntity>
}
