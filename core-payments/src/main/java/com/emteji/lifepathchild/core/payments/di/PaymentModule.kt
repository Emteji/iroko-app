package com.emteji.lifepathchild.core.payments.di

import com.emteji.lifepathchild.core.auth.repository.AuthRepository
import com.emteji.lifepathchild.core.data.database.UserDao
import com.emteji.lifepathchild.core.payments.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentModule {

    @Provides
    @Singleton
    fun providePaymentRepository(
        authRepository: AuthRepository,
        userDao: UserDao
    ): PaymentRepository {
        return PaymentRepository(authRepository, userDao)
    }
}
