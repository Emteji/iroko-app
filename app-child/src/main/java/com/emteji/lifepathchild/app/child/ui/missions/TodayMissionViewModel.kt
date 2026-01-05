package com.emteji.lifepathchild.app.child.ui.missions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emteji.lifepathchild.core.analytics.MicroTaskManager
import com.emteji.lifepathchild.core.data.model.MicroTaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DailyTaskUiModel(
    val id: String,
    val title: String,
    val instruction: String,
    val timeOfDay: TimeOfDay,
    val status: TaskStatus
)

enum class TimeOfDay { MORNING, AFTERNOON, EVENING }
enum class TaskStatus { PENDING, COMPLETED, MISSED }

@HiltViewModel
class TodayMissionViewModel @Inject constructor(
    private val microTaskManager: MicroTaskManager
) : ViewModel() {

    private val _dailyTasks = MutableStateFlow<List<DailyTaskUiModel>>(emptyList())
    val dailyTasks: StateFlow<List<DailyTaskUiModel>> = _dailyTasks.asStateFlow()

    init {
        loadDailyTasks()
    }

    private fun loadDailyTasks() {
        viewModelScope.launch {
            // Mock data based on PROMPT 3
            _dailyTasks.value = listOf(
                DailyTaskUiModel(
                    id = "1",
                    title = "Brush Teeth",
                    instruction = "2 minutes, circular motion.",
                    timeOfDay = TimeOfDay.MORNING,
                    status = TaskStatus.COMPLETED
                ),
                DailyTaskUiModel(
                    id = "2",
                    title = "Make Bed",
                    instruction = "Pull sheets up tight.",
                    timeOfDay = TimeOfDay.MORNING,
                    status = TaskStatus.PENDING
                ),
                DailyTaskUiModel(
                    id = "3",
                    title = "Homework",
                    instruction = "Math worksheet page 4.",
                    timeOfDay = TimeOfDay.AFTERNOON,
                    status = TaskStatus.PENDING
                ),
                DailyTaskUiModel(
                    id = "4",
                    title = "Read Book",
                    instruction = "15 minutes of quiet reading.",
                    timeOfDay = TimeOfDay.EVENING,
                    status = TaskStatus.PENDING
                )
            )
        }
    }
}
