package com.emteji.lifepathchild.app.parent.ui.community

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class CommunityRank(
    val rank: Int,
    val name: String,
    val score: String,
    val trend: String // "up", "down", "stable"
)

@HiltViewModel
class CommunityViewModel @Inject constructor() : ViewModel() {

    private val _rankings = MutableStateFlow<List<CommunityRank>>(emptyList())
    val rankings: StateFlow<List<CommunityRank>> = _rankings.asStateFlow()

    init {
        // Mock Data for MVP
        _rankings.value = listOf(
            CommunityRank(1, "The Adebayo Family", "98% Consistency", "up"),
            CommunityRank(2, "Sarah's Village", "95% Consistency", "up"),
            CommunityRank(3, "Kwame & Co", "92% Consistency", "stable"),
            CommunityRank(453, "You (Joshua)", "Top 23%", "up"), // Current User
            CommunityRank(454, "Amara's House", "Top 24%", "down")
        )
    }
}
