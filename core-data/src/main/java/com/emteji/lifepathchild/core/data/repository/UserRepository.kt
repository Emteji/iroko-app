package com.emteji.lifepathchild.core.data.repository

import com.emteji.lifepathchild.core.data.model.Child
import com.emteji.lifepathchild.core.data.model.VillageContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    // Parent onboarding state (mock for now)
    private var isParentOnboardingCompleted = false

    suspend fun setParentOnboardingCompleted(completed: Boolean) {
        isParentOnboardingCompleted = completed
    }
    
    suspend fun isParentOnboardingCompleted(): Boolean {
        return isParentOnboardingCompleted
    }

    suspend fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }

    suspend fun getCurrentChildId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }

    suspend fun getChildrenForParent(): Result<List<Child>> {
        return try {
            val userId = supabase.auth.currentUserOrNull()?.id
            
            // If real user is logged in, fetch from DB
            if (userId != null) {
                val children = supabase.postgrest.from("children")
                    .select {
                        filter { eq("user_id", userId) }
                    }.decodeList<Child>()
                Result.success(children)
            } else {
                // DEMO MODE: Return mock children if no auth (or failed auth)
                Result.success(listOf(
                    Child(id = "mock-1", userId = "mock-user", name = "Joshua", gritScore = 620),
                    Child(id = "mock-2", userId = "mock-user", name = "Amara", gritScore = 450)
                ))
            }
        } catch (e: Exception) {
            // Fallback for demo
            Result.success(listOf(
                Child(id = "mock-1", userId = "mock-user", name = "Joshua", gritScore = 620),
                Child(id = "mock-2", userId = "mock-user", name = "Amara", gritScore = 450)
            ))
        }
    }

    suspend fun createChildWithContext(child: Child, context: VillageContext): Result<Unit> {
        return try {
            // 1. Insert Child
            val insertedChild = supabase.postgrest.from("children").insert(child) {
                select()
            }.decodeSingle<Child>()

            // 2. Insert Village Context linked to Child
            val contextWithId = context.copy(childId = insertedChild.id)
            supabase.postgrest.from("village_context").insert(contextWithId)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun findChildByPairingCode(code: String): Result<Child?> {
        return try {
            // For MVP, we fetch ALL children and filter locally if 'like' operator is tricky with RLS
            // OR use RPC.
            // Simplified: If code is 8 chars, we fetch children where ID starts with code.
            // Supabase Postgrest 'like' or 'ilike'
            
            // NOTE: 'children' table RLS might prevent reading if not auth'd as parent.
            // For Child App (Anon), we need a specific RPC or permissive RLS for "pairing".
            // Here we assume open read for 'children' or a specific function.
            
            // Try to fetch real data
            val children = supabase.postgrest.from("children")
                .select().decodeList<Child>()
                
            val match = children.find { it.id.startsWith(code, ignoreCase = true) }
            
            if (match != null) {
                Result.success(match)
            } else {
                // FALLBACK FOR DEMO: If network fails or no match, and code looks like a UUID start,
                // we "Mock" a success so the app can be entered.
                // This enables the "Offline/Demo" flow where Parent App generated an ID but couldn't save it.
                if (code.length >= 8) {
                    Result.success(Child(id = "mock-child-id-$code", userId = "mock-user", name = "Child (Demo)"))
                } else {
                    Result.success(null)
                }
            }
        } catch (e: Exception) {
            // Fallback for demo
             if (code.length >= 8) {
                Result.success(Child(id = "mock-child-id-$code", userId = "mock-user", name = "Child (Demo)"))
             } else {
                Result.failure(e)
             }
        }
    }

    private var localPairedChildId: String? = null

    suspend fun savePairedChildId(childId: String) {
        localPairedChildId = childId
        // TODO: Save to DataStore/SharedPrefs
    }
    
    suspend fun getPairedChildId(): String? {
        return localPairedChildId
    }

    suspend fun getCurrentChild(): Result<Child> {
        val childId = localPairedChildId ?: "mock-1" // Fallback to mock-1 for dev
        // Fetch child details
        // Ideally reuse findChildByPairingCode logic or a new getChildById
        return Result.success(
             Child(id = childId, userId = "mock-user", name = "Joshua", gritScore = 620, xpTotal = 1250)
        )
    }
}
