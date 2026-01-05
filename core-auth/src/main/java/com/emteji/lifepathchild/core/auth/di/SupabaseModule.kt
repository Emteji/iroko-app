package com.emteji.lifepathchild.core.auth.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.functions.functions
import javax.inject.Singleton
import com.emteji.lifepathchild.core.auth.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        // Fallback to dummy values if BuildConfig is empty to prevent crash on launch
        val supabaseUrl = if (BuildConfig.SUPABASE_URL.isNotBlank()) BuildConfig.SUPABASE_URL else "https://placeholder.supabase.co"
        val supabaseKey = if (BuildConfig.SUPABASE_ANON_KEY.isNotBlank()) BuildConfig.SUPABASE_ANON_KEY else "placeholder-key"

        return createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Auth)
            install(Postgrest)
            install(Functions)
        }
    }
}
