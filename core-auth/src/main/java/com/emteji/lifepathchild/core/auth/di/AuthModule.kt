package com.emteji.lifepathchild.core.auth.di

import com.emteji.lifepathchild.core.auth.repository.AuthRepository
import com.emteji.lifepathchild.core.auth.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
