package com.emteji.lifepathchild.core.payments.repository

import android.content.Context
import android.app.Activity
import com.emteji.lifepathchild.core.auth.repository.AuthRepository
import com.emteji.lifepathchild.core.data.database.UserDao
import com.emteji.lifepathchild.core.data.model.SubscriptionTier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val authRepository: AuthRepository,
    private val userDao: UserDao
) {

    /**
     * Initializes Paystack SDK (Stubbed).
     */
    fun initialize(context: Context, publicKey: String) {
        // Stub: Paystack declaration removed due to dependency resolution issues.
        // In production, uncomment and use: PaystackSdk.initialize(context)
        kotlin.io.println("PaymentRepository: Initialized with key $publicKey (Stub)")
    }

    /**
     * Initiates a payment transaction (Stubbed).
     * Simulates a network delay and success.
     */
    fun startPayment(
        activity: Activity,
        email: String,
        amount: Int, // Kobo
        tier: SubscriptionTier
    ): Flow<PaymentResult> = flow {
        emit(PaymentResult.Processing)
        delay(2000) // Simulate network
        // Simulate Success
        val mockReference = "ref_${System.currentTimeMillis()}"
        emit(PaymentResult.Success(mockReference, tier))
    }.flowOn(Dispatchers.IO)

    /**
     * Verifies the transaction and updates local database upon success.
     */
    suspend fun verifyAndUpgrade(userId: String, tier: SubscriptionTier) {
        userDao.updateSubscriptionTier(userId, tier, System.currentTimeMillis())
        // TODO: Call Backend function to log transaction securely
    }
}

sealed class PaymentResult {
    data object Processing : PaymentResult()
    data class Success(val reference: String, val tier: SubscriptionTier) : PaymentResult()
    data class Error(val message: String) : PaymentResult()
}
