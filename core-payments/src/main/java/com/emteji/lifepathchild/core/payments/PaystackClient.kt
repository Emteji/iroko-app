package com.emteji.lifepathchild.core.payments

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaystackClient @Inject constructor() {

    fun initialize(amount: Long, currency: String, email: String): String {
        return "https://example.com/mock-paystack-checkout?amount=$amount&currency=$currency&email=$email"
    }
}

