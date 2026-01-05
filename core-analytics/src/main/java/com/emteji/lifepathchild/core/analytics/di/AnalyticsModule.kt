package com.emteji.lifepathchild.core.analytics.di

import com.emteji.lifepathchild.core.ai.AIService
import com.emteji.lifepathchild.core.analytics.GuidanceEngine
import com.emteji.lifepathchild.core.analytics.InterestEngine
import com.emteji.lifepathchild.core.analytics.MicroTaskManager
import com.emteji.lifepathchild.core.analytics.PsychologicalEngine
import com.emteji.lifepathchild.core.data.repository.MicroTaskRepository
import com.emteji.lifepathchild.core.data.repository.SignalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideInterestEngine(signalRepository: SignalRepository): InterestEngine {
        return InterestEngine(signalRepository)
    }

    @Provides
    @Singleton
    fun provideMicroTaskManager(
        microTaskRepository: MicroTaskRepository,
        interestEngine: InterestEngine
    ): MicroTaskManager {
        return MicroTaskManager(microTaskRepository, interestEngine)
    }

    @Provides
    @Singleton
    fun provideGuidanceEngine(
        interestEngine: InterestEngine,
        aiService: AIService,
        psychologicalEngine: PsychologicalEngine
    ): GuidanceEngine {
        return GuidanceEngine(interestEngine, aiService, psychologicalEngine)
    }
}
