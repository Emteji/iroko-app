package com.emteji.lifepathchild.core.ai.di

import com.emteji.lifepathchild.core.ai.AIScheduler
import com.emteji.lifepathchild.core.ai.AISchedulerStub
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AiModule {
    @Binds
    abstract fun bindAIScheduler(
        scheduler: AISchedulerStub
    ): AIScheduler
}
