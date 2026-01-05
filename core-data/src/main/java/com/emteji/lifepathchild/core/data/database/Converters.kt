package com.emteji.lifepathchild.core.data.database

import androidx.room.TypeConverter
import com.emteji.lifepathchild.core.data.model.SignalType
import com.emteji.lifepathchild.core.data.model.RewardStatus
import com.emteji.lifepathchild.core.data.model.TaskCategory
import com.emteji.lifepathchild.core.data.model.SubscriptionTier

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    fun fromSignalType(value: SignalType?): String? = value?.name

    @TypeConverter
    fun toSignalType(value: String?): SignalType? = value?.let { SignalType.valueOf(it) }

    @TypeConverter
    fun fromRewardStatus(value: RewardStatus?): String? = value?.name

    @TypeConverter
    fun toRewardStatus(value: String?): RewardStatus? = value?.let { RewardStatus.valueOf(it) }

    @TypeConverter
    fun fromTaskCategory(value: TaskCategory?): String? = value?.name

    @TypeConverter
    fun toTaskCategory(value: String?): TaskCategory? = value?.let { TaskCategory.valueOf(it) }

    @TypeConverter
    fun fromSubscriptionTier(value: SubscriptionTier?): String? = value?.name

    @TypeConverter
    fun toSubscriptionTier(value: String?): SubscriptionTier? = value?.let { SubscriptionTier.valueOf(it) }
}
