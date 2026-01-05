package com.emteji.lifepathchild.core.auth.repository

import com.emteji.lifepathchild.core.auth.model.UserRole
import com.emteji.lifepathchild.core.auth.session.SessionManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class AuthUserRow(val role: String)

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun logout()
    suspend fun resetPassword(email: String)
    suspend fun getCurrentRole(): UserRole
}

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient,
    private val sessionManager: SessionManager
) : AuthRepository {

    private val auth = supabaseClient.auth

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val session = auth.currentSessionOrNull()
            // Fetch Role
            val role = getCurrentRole()
            sessionManager.saveSession(session?.accessToken ?: "", session?.refreshToken ?: "", role.name) 
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
        sessionManager.clearSession()
    }

    override suspend fun resetPassword(email: String) {
        auth.resetPasswordForEmail(email)
    }

    override suspend fun getCurrentRole(): UserRole {
        val uid = auth.currentSessionOrNull()?.user?.id ?: return UserRole.UNKNOWN
        return try {
            val user = supabaseClient.postgrest["users"].select {
                filter { eq("id", uid) }
            }.decodeSingleOrNull<AuthUserRow>()
            
            user?.role?.uppercase()?.let { 
                try { UserRole.valueOf(it) } catch(e: Exception) { UserRole.UNKNOWN }
            } ?: UserRole.UNKNOWN
        } catch (e: Exception) {
            e.printStackTrace()
            UserRole.UNKNOWN
        }
    }
}
