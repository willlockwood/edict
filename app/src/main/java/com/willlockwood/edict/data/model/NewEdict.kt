package com.willlockwood.edict.data.model

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.willlockwood.edict.utils.TimeHelper

@Entity(
    tableName="edicts"
)
data class NewEdict(
    var type: Type? = null,
    var scope: Scope? = null,
    var scalable: Boolean? = null,
    var action: String? = null,
    var unitVar: Int? = null,
    var unit: String? = null,
    var days: List<Int>? = null,
    var daysInt: Int? = null,
    var daysText: String? = null,
    var timeType: TimeType? = null,
    var timeStart: Int? = 0,
    var timeEnd: Int? = 1440,
//    var timeStart: Int? = null,
//    var timeEnd: Int? = null,
    var timeText: String? = null,
    var checkInStartType: CheckInStartType? = null,
    var checkInStart: Int? = null,
    var checkInEndType: CheckInEndType? = null,
    var checkInEnd: Int? = null,

    var notifications: List<Pair<NotificationType, Int?>> = listOf(Pair(NotificationType.CHECK_IN_BEFORE_END, 15))


) : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    enum class Type { RESTRICTION, ROUTINE }
    enum class Scope { DAILY, WEEKLY, SOME_DAYS, VAR_DAYS }
    enum class TimeType { ALL_DAY, AFTER_TIME, AFTER_TEXT, BEFORE_TIME, BEFORE_TEXT, BETWEEN_TIME, BETWEEN_TEXT, WHEN, WHILE, AT }
    enum class CheckInStartType { BEFORE, AT }
    enum class CheckInEndType { MORNING, MIDDAY, EVENING, AT }
    enum class NotificationType { START, END, BEFORE_END, CHECK_IN_START, CHECK_IN_END, CHECK_IN_BEFORE_END, AT }

    enum class UserEditingOrCreating { EDITING, CREATING }

    fun removeNotification(type: NotificationType) {
        notifications = notifications.filter { it.first != type }
    }
    fun addToNotifications(type: NotificationType, minutes: Int?) {
        removeNotification(type)
        notifications = notifications.plus(Pair(type, minutes)).sortedBy { it.first.ordinal }
    }
    fun hasNotification(type: NotificationType): Boolean {
        return notifications.any { it.first == type }
    }
    fun getNotification(type: NotificationType): Pair<NotificationType, Int?>? {
        return if (hasNotification(type)) {
            notifications.filter { it.first == type }[0]
        } else { null }
    }

    fun daysString(): String {
        return when (scope) {
            Scope.DAILY -> "Every day, "
            Scope.WEEKLY -> "Every week, "
            Scope.SOME_DAYS -> when (days) {
                null -> when (daysText) {
                    null -> "On ..., "
                    else -> "On $daysText, "
                }
                else -> {
                    val daysList = days!!
                    var daysStrings = daysList.sorted().map { when (it) {
                        1 -> "Su"
                        2 -> "M"
                        3 -> "T"
                        4 -> "W"
                        5 -> "Th"
                        6 -> "F"
                        7 -> "S"
                        else -> ""
                    } }
                    if (daysStrings.size > 1) {
                        var lastDay = daysStrings.last()
                        daysStrings = daysStrings.dropLast(1).plus("and $lastDay")
                    }
                    val daysText = when (daysStrings.size) {
                        0 -> "..."
                        1 -> daysStrings[0]
                        2 -> daysStrings.joinToString(" ")
                        else -> daysStrings.joinToString(", ")
                    }
                    "On $daysText, "
                }
            }
            Scope.VAR_DAYS -> when (daysInt) {
                null -> "Every _ days, "
                else -> "Every $daysInt days, "
            }
            null -> "..."
        }
    }

    fun ruleString(): String {
        val verb = when (action) {
            "" -> "..."
            null -> "..."
            else -> action
        }
        val number = when (unitVar) {
            null -> ""
            else -> " $unitVar"
        }
        val noun = when (unit) {
            null -> ""
            else -> " $unit"
        }
        return when (type) {
            Type.RESTRICTION -> when (scalable) {
                true -> "$verb$number$noun"
                false -> "$verb"
                null -> "..."
            }
            Type.ROUTINE -> when (scalable) {
                true -> "$verb$number$noun"
                false -> "$verb"
                null -> "..."
            }
            null -> "..."
        }
    }

    fun timeString(): String {
        val startTime = when (timeStart) {
            null -> "..."
            else -> TimeHelper.minutesToTimeString(timeStart)
        }
        val endTime = when (timeEnd) {
            null -> "..."
            else -> TimeHelper.minutesToTimeString(timeEnd)
        }
        val text = when (timeText) {
            null -> "..."
            else -> timeText!!
        }

        return when (timeType) {
            TimeType.ALL_DAY -> "all day"
            TimeType.AFTER_TIME -> "after $startTime"
            TimeType.AFTER_TEXT -> "after $text"
            TimeType.BEFORE_TIME -> "before $endTime"
            TimeType.BEFORE_TEXT -> "before $text"
            TimeType.BETWEEN_TIME -> "between $startTime and $endTime"
            TimeType.BETWEEN_TEXT -> "between $text"
            TimeType.WHEN -> "when $text"
            TimeType.WHILE -> "while $text"
            TimeType.AT -> "at $text"
            null -> "..."
        }
    }

    override fun toString(): String {

        val days = when (scope) {
            Scope.DAILY -> "Every day, "
            Scope.WEEKLY -> "Every week, "
            Scope.SOME_DAYS -> when (daysText) {
                null -> "On ${days!!.map { 
                    when (it) {
                        1 -> "Sun."
                        2 -> "Mon."
                        3 -> "Tue."
                        4 -> "Wed."
                        5 -> "Thu."
                        6 -> "Fri."
                        7 -> "Sat."
                        else -> ""
                    }
                }.joinToString(", ")}, "
                else -> "${daysText!!.capitalize()}, "
            }
            Scope.VAR_DAYS -> "Every _ days,"
            null -> ""
        }

        val rule = when (scalable) {
            true -> "$action $unitVar $unit"
            false -> "$action"
            null -> ""
        }

        val time = when (timeType) {
            TimeType.ALL_DAY -> ""

            TimeType.AFTER_TIME -> " after ${TimeHelper.minutesToTimeString(timeStart)}"
            TimeType.AFTER_TEXT -> " after $timeText"

            TimeType.BEFORE_TIME -> " before ${TimeHelper.minutesToTimeString(timeEnd)}"
            TimeType.BEFORE_TEXT -> " before $timeText"

            TimeType.BETWEEN_TIME -> " between ${TimeHelper.minutesToTimeString(timeStart)} and ${TimeHelper.minutesToTimeString(timeEnd)}"
            TimeType.BETWEEN_TEXT -> " after $timeText"

            TimeType.WHEN -> " when $timeText"
            TimeType.WHILE -> " while $timeText"
            TimeType.AT -> " at $timeText"
            null -> ""
        }
        return "$days$rule$time."
    }

    fun notificationsString(): String {
        return when (notifications.size) {
            0 -> "None"
            else -> notifications.map { when (it.first) {
                NotificationType.START -> "when it starts"
                NotificationType.END -> "when it ends"
                NotificationType.BEFORE_END -> "${it.second} min. before it ends"
                NotificationType.CHECK_IN_START -> "when check-in starts"
                NotificationType.CHECK_IN_END -> "when check-in ends"
                NotificationType.CHECK_IN_BEFORE_END -> "${it.second} min. before check-in ends"
                NotificationType.AT -> "at ${TimeHelper.minutesToTimeStringShort(it.second)}"
            } }.joinToString(", ").capitalize()
        }
    }

    fun notificationsToString(list: List<Pair<NotificationType, Int?>>): String {
        return list.joinToString(";") { "${it.first.name},${it.second}" }
//        return list.joinToString(";") { "${it.first.name},${it.second}" }
    }

    fun notificationsFromString(value: String): List<Pair<NotificationType, Int?>> {
        if (value == "") {
            return emptyList()
        }
        val pairsList = value.split(";")
        var returnList = emptyList<Pair<NotificationType, Int?>>()
        for (pair in pairsList) {
            val type = NotificationType.valueOf(pair.split(",")[0])
            val number = when (pair.split(",")[1]) {
                "" -> null
                "null" -> null
                null -> null
                else -> pair.split(",")[1].toInt()
            }
            returnList = returnList.plus(Pair(type, number))
        }
        return returnList
    }

    fun populateFromBundle(bundle: Bundle): NewEdict {
        type = bundle.get("type") as Type?
        scope = bundle.get("scope") as Scope?
        scalable = bundle.get("scalable") as Boolean?
        action = bundle.getString("action")
        unitVar = bundle.get("unitVar") as Int?
        unit = bundle.getString("unit")
        days = bundle.get("days") as List<Int>?
        daysInt = bundle.get("daysInt") as Int?
        daysText = bundle.getString("daysText")
        timeType = bundle.get("timeType") as TimeType?
        timeStart = bundle.get("timeStart") as Int?
        timeEnd = bundle.get("timeEnd") as Int?
        timeText = bundle.getString("timeText")
        checkInStartType = bundle.get("checkInStartType") as CheckInStartType?
        checkInStart = bundle.get("checkInStart") as Int?
        checkInEndType = bundle.get("checkInEndType") as CheckInEndType?
        checkInEnd = bundle.get("checkInEnd") as Int?
        notifications = notificationsFromString(bundle.getString("notifications", ""))
        return this
    }

    fun toBundle(): Bundle {
        return bundleOf(
            "type" to type,
            "scope" to scope,
            "scalable" to scalable,
            "action" to action,
            "unitVar" to unitVar,
            "unit" to unit,
            "days" to days,
            "daysInt" to daysInt,
            "daysText" to daysText,
            "timeType" to timeType,
            "timeStart" to timeStart,
            "timeEnd" to timeEnd,
            "timeText" to timeText,
            "checkInStartType" to checkInStartType, "checkInStart" to checkInStart,
            "checkInEndType" to checkInEndType,     "checkInEnd" to checkInEnd,
            "notifications" to notificationsToString(notifications)
        )
    }

}
