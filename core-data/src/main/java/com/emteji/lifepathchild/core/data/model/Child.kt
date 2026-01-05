package com.emteji.lifepathchild.core.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Child(
    val id: String,
    @SerialName("user_id") val userId: String,
    val name: String,
    
    // Core Metrics
    @SerialName("grit_score") val gritScore: Int = 0,
    @SerialName("omoluabi_score") val omoluabiScore: Int = 0,
    @SerialName("xp_total") val xpTotal: Int = 0,
    
    // Deep Profiling Data (Nullable as they are populated during onboarding wizard)
    val age: Int? = null,
    val gender: String? = null,
    @SerialName("school_type") val schoolType: String? = null, // Public, Private, Homeschool
    @SerialName("academic_level") val academicLevel: String? = null, // Below Average, Average, Above Average, Gifted
    
    // Psychological Profile
    val temperament: String? = null, // Choleric, Sanguine, Phlegmatic, Melancholic
    @SerialName("attention_span") val attentionSpan: String? = null, // Low, Medium, High
    @SerialName("emotional_sensitivity") val emotionalSensitivity: String? = null, // Low, Medium, High
    
    // Household & Culture
    @SerialName("discipline_style") val disciplineStyle: String? = null, // Authoritative, Authoritarian, Permissive, Gentle
    @SerialName("primary_language") val primaryLanguage: String? = null,
    @SerialName("cultural_background") val culturalBackground: String? = null,
    
    // Issues & Goals
    @SerialName("behavioral_concerns") val behavioralConcerns: List<String> = emptyList(),
    @SerialName("parent_expectations") val parentExpectations: List<String> = emptyList()
)
