package com.willlockwood.edict.data.model

import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willlockwood.edict.utils.EdictHelper
import com.willlockwood.edict.utils.TimeHelper

@Entity(
    tableName="edicts"
)
data class Edict(
    var type: String = "",
    var activity: String = "",
    var detailType: String = "",
    var detailMinutes: Int? = null,
    var detailMinutes2: Int? = null,

    var atText: String? = null,
    var whileText: String? = null,
    var whenText: String? = null,

    var onType: String? = null,
    var onDaysOfTheWeek: String? = null,
    var onText: String? = null,

    var everyType: String? = null,
    var everyTimeText: String? = null,
    var everyNumber: Int? = null,
    var everyNumberTime: String? = null,
    var numberTime: Int? = null,
    var numberTimesPerTime: String? = null,

    var currentStreak: Int = 0,
    var level: Int = 0,

    var notifyStartMinutes: Int? = null,
    var notifyEndMinutes: Int? = null,
    var notifyAtMinutes: Int? = null,

    var deadlineType: String? = null,
    var deadlineMinutes: Int = 1200
) : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    private fun onDaysString(text: String, andOr: String): String {
        return if (text.contains(", ")) {
            val list = text.split(", ")
            when (val size = list.size) {
                1 -> text
                2 -> "${list.subList(0, size - 1).joinToString(", ")}, $andOr ${list[size-1]}"
                else -> "${list.subList(0, size - 1).joinToString(", ")}, $andOr ${list[size-1]}"
            }
        } else {
            text
        }
    }

    fun getEdictType(): EdictHelper.EdictType {
        return EdictHelper.getEdictType(this)
    }

    fun getErrorStatus(): EdictHelper.ErrorStatus {
        return EdictHelper.getErrorStatus(this)
    }

    fun isActiveToday(): Boolean {
        return EdictHelper.getEdictIsActiveToday(this)
    }

    fun createEdictSession(): EdictSession {
        var notifyMap = emptyMap<String, Int>()
        if (notifyStartMinutes != null) { notifyMap = notifyMap.plus(Pair("start", notifyStartMinutes!!)) }
        if (notifyEndMinutes != null) { notifyMap = notifyMap.plus(Pair("end", notifyEndMinutes!!)) }
        if (notifyAtMinutes != null) { notifyMap = notifyMap.plus(Pair("at", notifyAtMinutes!!)) }

        val endTime = when (detailType) {
            "before" -> detailMinutes!!
            "between" -> detailMinutes2!!
            else -> deadlineMinutes
        }
        val startTime = when (detailType) {
            "after" -> detailMinutes!!
            "between" -> detailMinutes!!
            else -> 0
        }

        return EdictSession(id, this.toString(),
                                notificationMinutes = notifyMap,
                                deadlineMinutes = deadlineMinutes,
                                startMinutes = startTime,
                                endMinutes = endTime,
                                startingLevel = level)
    }

    fun addToStreak() {
        currentStreak += 1
        refreshLevel()
    }

    fun resetStreak() {
        currentStreak = 0
    }

    private fun refreshLevel() {
        level = EdictHelper.getLevel(this)
    }

    override fun toString(): String {
        val days = onDaysOfTheWeek
        return when (type) {
            "Routine:" -> {
                when (detailType) {
                    "every" -> when (everyType) {
                        "time" -> "${activity.capitalize()} $detailType $everyType $everyTimeText."
                        "morning" -> "${activity.capitalize()} $detailType $everyType."
                        "night" -> "${activity.capitalize()} $detailType $everyType."
                        "day" -> "${activity.capitalize()} $detailType $everyType."
                        "week" -> "${activity.capitalize()} $detailType $everyType."
                        "#" -> "${activity.capitalize()} $detailType $everyNumber $everyNumberTime."
                        else -> "$type else $activity $detailType $everyType."
                    }
                    "#" -> "${activity.capitalize()} $numberTime times per $numberTimesPerTime."
                    "on" -> when {
                        onDaysOfTheWeek != null -> "${activity.capitalize()} $detailType ${onDaysString(days!!, "and")}."
                        onText != null -> "${activity.capitalize()} $detailType $onText."
                        else -> "${activity.capitalize()} $detailType ..."
                    }
                    else -> "FIGURE THIS OUT"
                }
            }
            else -> {
                when (detailType) {
                    "at" -> "$type $activity $detailType $atText."
                    "after" -> "$type $activity $detailType ${TimeHelper.minutesToTimeString(detailMinutes)}."
                    "before" -> "$type $activity $detailType ${TimeHelper.minutesToTimeString(detailMinutes)}."
                    "between" -> "$type $activity $detailType ${TimeHelper.minutesToTimeString(detailMinutes)} and ${TimeHelper.minutesToTimeString(detailMinutes2)}."
                    "while" -> "$type $activity $detailType $whileText."
                    "when" -> "$type $activity $detailType $whenText."
                    "ever" -> "$type $activity, $detailType."
                    "on" -> when {
                        onDaysOfTheWeek != null -> "$type $activity $detailType ${onDaysString(days!!, "or")}."
                        onText != null -> "$type $activity $detailType $onText."
                        else -> "$type $activity $detailType ..."
                    }
                    else -> "FIGURE THIS OUT"
                }
            }
        }
    }
}