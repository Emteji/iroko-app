package com.emteji.lifepathchild.app.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.auth.SupabaseProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.Instant

data class ChildLoginState(
    val loggedIn: Boolean = false,
    val sessionValid: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ChildLoginViewModel @Inject constructor(private val provider: SupabaseProvider) : ViewModel() {
    private val _state = MutableStateFlow(ChildLoginState())
    val state: StateFlow<ChildLoginState> = _state

    fun login(email: String, password: String, deviceId: String) {
        viewModelScope.launch {
            try {
                provider.client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                val user: UserInfo? = provider.client.auth.currentUserOrNull()
                if (user == null) {
                    _state.value = ChildLoginState(loggedIn = false, sessionValid = false, error = "Login failed")
                    return@launch
                }
                val children = provider.client.postgrest["children"].select { filter { eq("user_id", user.id) } }
                val child = children.decodeList<com.emteji.lifepathchild.core.data.model.Child>().firstOrNull()
                if (child == null) {
                    _state.value = ChildLoginState(loggedIn = true, sessionValid = false, error = "No child profile")
                    return@launch
                }
                val sessions = provider.client.postgrest["child_sessions"].select {
                    filter {
                        eq("child_id", child.id)
                        eq("is_active", true)
                        eq("revoked", false)
                    }
                    limit(1)
                }
                val active = sessions.decodeList<com.emteji.lifepathchild.core.data.model.ChildSession>().firstOrNull()
                val valid = active != null &&
                        active.isActive &&
                        !active.revoked &&
                        active.deviceId == deviceId &&
                        isSessionActive(active.sessionEnd)
                _state.value = ChildLoginState(loggedIn = true, sessionValid = valid, error = if (valid) null else "Session invalid. Ask parent.")
            } catch (e: Exception) {
                _state.value = ChildLoginState(loggedIn = false, sessionValid = false, error = e.message)
            }
        }
    }

    private fun isSessionActive(sessionEnd: String?): Boolean {
        if (sessionEnd.isNullOrBlank()) return true
        return try {
            Instant.parse(sessionEnd).isAfter(Instant.now())
        } catch (e: Exception) {
            true
        }
    }
}
