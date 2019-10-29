package com.willlockwood.edict.data.model

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.databinding.BaseObservable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.willlockwood.edict.data.converter.EdictTypeConverters
import com.willlockwood.edict.utils.TimeHelper
import org.threeten.bp.DayOfWeek

@Entity(
    tableName="new_edicts"
)
data class NewEdict(

    @TypeConverters(EdictTypeConverters::class)
    var type: Type? = null,

    @TypeConverters(EdictTypeConverters::class)
    var scope: Scope? = null,
    var scalable: Boolean? = null,
    var action: String? = null,
    var unitVar: Int? = null,
    var unit: String? = null,

    @TypeConverters(EdictTypeConverters::class)
    var days: List<DayOfWeek> = emptyList(),
    var daysInt: Int? = null,
    var daysText: String? = null,

    @TypeConverters(EdictTypeConverters::class)
    var timeType: TimeType? = null,
    var timeStart: Int? = 0,
    var timeEnd: Int? = 1440,
    var timeText: String? = null,
//    var checkInStartType: CheckInStartType? = null,
    var checkInStart: Int? = null,
//    var checkInEndType: CheckInEndType? = null,
    var checkInEnd: Int? = null,

    @TypeConverters(EdictTypeConverters::class)
    var notifications: List<Pair<NotificationType, Int?>> = listOf(Pair(NotificationType.CHECK_IN_BEFORE_END, 15))


) : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    enum class Type { RESTRICTION, ROUTINE }
    enum class Scope { DAILY, WEEKLY, SOME_DAYS, TEXT_DAYS, VAR_DAYS }
    enum class TimeType { ALL_DAY, AFTER_TIME, AFTER_TEXT, BEFORE_TIME, BEFORE_TEXT, BETWEEN_TIME, BETWEEN_TEXT, WHEN, WHILE, AT }
//    enum class CheckInStartType { BEFORE, AT }
//    enum class CheckInEndType { MORNING, MIDDAY, EVENING, AT }
    enum class NotificationType { START, END, BEFORE_END, CHECK_IN_START, CHECK_IN_END, CHECK_IN_BEFORE_END, AT }

//    enum class UserEditingOrCreating { EDITING, CREATING }

    fun getDaysSubheader(): String {
        return when (scope) {
            Scope.DAILY -> "Every day"
            Scope.WEEKLY -> "Every week"
            Scope.SOME_DAYS -> when (days) {
                null -> "On ..."
                else -> {
                    val daysList = days!!
                    var daysStrings = daysList.sorted().map { when (it) {
                        DayOfWeek.SUNDAY -> "Su"
                        DayOfWeek.MONDAY -> "M"
                        DayOfWeek.TUESDAY -> "T"
                        DayOfWeek.WEDNESDAY -> "W"
                        DayOfWeek.THURSDAY -> "Th"
                        DayOfWeek.FRIDAY -> "F"
                        DayOfWeek.SATURDAY -> "S"
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
                    "On $daysText"
                }
            }
            Scope.TEXT_DAYS -> "On ${daysText ?: "..."}"
            Scope.VAR_DAYS -> "Every ${daysInt ?: "..."} days,"
            null -> "..."
        }
    }

    fun getActionHeader(): String { return when (type) {
        Type.RESTRICTION -> "Restricted action"
        Type.ROUTINE -> "Routine action"
        null -> ""
    }}
    fun getActionSubheader(): String {
        var verb = action
        var number = unitVar.toString()
        var noun = unit
        if (action == null && unitVar == null && unit == null) {
            return "None"
        }

        if (verb == "" || verb == null)         verb = "..."
        if (number == "" || number == "null")   number = "..."
        if (noun == "" || noun == null)         noun = "..."

        return when (scalable) {
            true -> "$verb $number $noun"
            false -> "$verb"
            else -> "..."
    } }

    fun getTimesSubheader(): String {
        val startTime = when (timeStart) {
            null -> "..."
            else -> TimeHelper.minutesToTimeStringShort(timeStart)
        }
        val endTime = when (timeEnd) {
            null -> "..."
            else -> TimeHelper.minutesToTimeStringShort(timeEnd)
        }
        val text = timeText ?: "..."

        return when (timeType) {
            TimeType.ALL_DAY -> "All the time"
            TimeType.AFTER_TIME -> "After $startTime"
            TimeType.BEFORE_TIME -> "Before $startTime"
            TimeType.BETWEEN_TIME -> "Between $startTime and $endTime"
            TimeType.AFTER_TEXT -> "After $text"
            TimeType.BEFORE_TEXT -> "Before $text"
            TimeType.BETWEEN_TEXT -> "Between $text"
            TimeType.WHEN -> "When $text"
            TimeType.WHILE -> "While $text"
            TimeType.AT -> "At $text"
            null -> "None"
        }
    }

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
//    fun getNumberOfNotifications(): Int { return notifications.si
//    }
    fun toggleDayOfWeek(value: DayOfWeek) {
        days = if (days.isEmpty()) {
            listOf(value)
        } else if (days.contains(value)) {
            days.filter { it != value }
        } else {
            days.plus(value).sorted()
        }
    }

    fun getActionHints(): Triple<String?, String?, String?> {
        val nullTriple = Triple(null, null, null)
        return when (type) {
            Type.RESTRICTION -> when (scalable) {
                true -> Triple("only smoke", "2", "cigarettes")
                false -> Triple("don't drink", null, null)
                null -> nullTriple
            }
            Type.ROUTINE -> when (scalable) {
                true -> Triple("meditate for", "30", "minutes")
                false -> Triple("clean your room", null, null)
                null -> nullTriple
            }
            null -> nullTriple
        }
    }

    fun daysString(): String {
        return when (scope) {
            Scope.DAILY -> "Every day,"
            Scope.WEEKLY -> "Every week,"
            Scope.SOME_DAYS -> when (days) {
                null -> "On ${daysText ?: "..."},"
                else -> {
                    val daysList = days!!
                    var daysStrings = daysList.sorted().map { when (it) {
                        DayOfWeek.SUNDAY -> "Su"
                        DayOfWeek.MONDAY -> "M"
                        DayOfWeek.TUESDAY -> "T"
                        DayOfWeek.WEDNESDAY -> "W"
                        DayOfWeek.THURSDAY -> "Th"
                        DayOfWeek.FRIDAY -> "F"
                        DayOfWeek.SATURDAY -> "S"
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
                    "On $daysText,"
                }
            }
            Scope.TEXT_DAYS -> "On ${daysText ?: "..."},"
            Scope.VAR_DAYS -> "Every ${daysInt ?: "..."} days,"
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
        val text = timeText ?: "..."
//        val text = when (timeText) {
//            null -> "..."
//            else -> timeText!!
//        }

        return when (timeType) {
            TimeType.ALL_DAY ->         "all day"
            TimeType.AFTER_TIME ->      "after $startTime"
            TimeType.AFTER_TEXT ->      "after $text"
            TimeType.BEFORE_TIME ->     "before $endTime"
            TimeType.BEFORE_TEXT ->     "before $text"
            TimeType.BETWEEN_TIME ->    "between $startTime and $endTime"
            TimeType.BETWEEN_TEXT ->    "between $text"
            TimeType.WHEN ->            "when $text"
            TimeType.WHILE ->           "while $text"
            TimeType.AT ->              "at $text"
            null ->                     "..."
        }
    }

    override fun toString(): String {
        var string = "${daysString()} ${ruleString()} ${timeString()}."
        return string
            .replace("..., ...", "...")
            .replace("....", "...")
            .replace("... ... ...", "...")
            .replace("... ...", "...")
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

    fun notificationsToString(list: List<Pair<NewEdict.NotificationType, Int?>>): String {
        return list.joinToString(";") { "${it.first.name},${it.second}" }
//        return list.joinToString(";") { "${it.first.name},${it.second}" }
    }

    fun notificationsFromString(value: String): List<Pair<NewEdict.NotificationType, Int?>> {
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
        days = bundle.get("days") as List<DayOfWeek>
        daysInt = bundle.get("daysInt") as Int?
        daysText = bundle.getString("daysText")
        timeType = bundle.get("timeType") as TimeType?
        timeStart = bundle.get("timeStart") as Int?
        timeEnd = bundle.get("timeEnd") as Int?
        timeText = bundle.getString("timeText")
//        checkInStartType = bundle.get("checkInStartType") as CheckInStartType?
//        checkInEndType = bundle.get("checkInEndType") as CheckInEndType?
        checkInStart = bundle.get("checkInStart") as Int?
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
//            "checkInStartType" to checkInStartType,
//            "checkInEndType" to checkInEndType,
            "checkInStart" to checkInStart,
            "checkInEnd" to checkInEnd,
            "notifications" to notificationsToString(notifications)
        )
    }

}
