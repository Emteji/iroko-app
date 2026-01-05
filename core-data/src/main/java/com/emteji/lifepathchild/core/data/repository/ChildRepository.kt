package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.model.ChildProfile
import com.emteji.lifepathchild.core.data.model.Child
import com.emteji.lifepathchild.core.data.model.VillageContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChildRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    suspend fun getChildrenForParent(parentId: String): Result<List<ChildProfile>> {
        return try {
            val userId = supabase.auth.currentUserOrNull()?.id ?: parentId
            
            // If real user is logged in, fetch from DB
            val children = supabase.postgrest.from("children")
                .select {
                    filter { eq("user_id", userId) }
                }.decodeList<Child>()
            
            val profiles = children.map { 
                ChildProfile(
                    id = it.id, 
                    name = it.name, 
                    gritScore = it.gritScore,
                    xpTotal = it.xpTotal
                ) 
            }
            Result.success(profiles)
        } catch (e: Exception) {
            // Fallback for demo
            Result.success(listOf(
                ChildProfile(id = "mock-1", name = "Joshua", gritScore = 620, xpTotal = 1250),
                ChildProfile(id = "mock-2", name = "Amara", gritScore = 450, xpTotal = 800)
            ))
        }
    }
}
