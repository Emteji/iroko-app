package com.emteji.lifepathchild.core.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject
import javax.inject.Singleton
import io.github.jan.supabase.auth.auth

@Singleton
class SupabaseProvider @Inject constructor(@ApplicationContext context: Context) {
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Functions)
    }

    fun currentAccessToken(): String? = client.auth.currentSessionOrNull()?.accessToken
}
