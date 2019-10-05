package com.willlockwood.edict.utils

import com.willlockwood.edict.data.model.Edict
import org.threeten.bp.OffsetDateTime

object EdictHelper {

    enum class EdictType {
        NEVER_AFTER, NEVER_AT, NEVER_BETWEEN, NEVER_BEFORE, NEVER_EVER, NEVER_ON_DAYS, NEVER_ON, NEVER_WHEN, NEVER_WHILE,
        ONLY_AFTER, ONLY_AT, ONLY_BEFORE, ONLY_BETWEEN, ONLY_ON_DAYS, ONLY_ON, ONLY_WHEN, ONLY_WHILE,
        DO_EVERY_DAY, DO_EVERY_MORNING, DO_EVERY_NIGHT, DO_EVERY_WEEK, DO_EVERY_NUM_DAYS, DO_EVERY_NUM_WEEKS, DO_EVERY_TIME, DO_ON_DAYS, DO_ON, DO_NUM_TIMES_PER_DAY, DO_NUM_TIMES_PER_WEEK,
        ERROR
    }

    fun getEdictIsActiveToday(edict: Edict): Boolean {
        val today = OffsetDateTime.now().dayOfWeek.name
        return when (edict.getEdictType()) {
            EdictType.ERROR -> false
            EdictType.NEVER_ON_DAYS -> edict.onDaysOfTheWeek!!.toLowerCase().contains(today.toLowerCase(), true)
            EdictType.ONLY_ON_DAYS -> edict.onDaysOfTheWeek!!.toLowerCase().contains(today.toLowerCase(), true)
            EdictType.DO_ON_DAYS -> edict.onDaysOfTheWeek!!.toLowerCase().contains(today.toLowerCase(), true)
            else -> true
        }
    }

    fun getLevel(edict: Edict): Int {
        val streak = edict.currentStreak
        return when {
            streak < 1 -> 0
            streak < 2 -> 1
            streak < 4 -> 2
            streak < 8 -> 3
            streak < 16 -> 4
            streak < 32 -> 5
            streak < 64 -> 6
            streak < 128 -> 7
            else -> 8
        }
    }

    enum class ErrorStatus { READY, EMPTY_ACTIVITY, ACTIVITY_STILL_DEFAULT, BEFORE_NOT_BEFORE_AFTER, WHEN_STILL_DEFAULT, WHILE_STILL_DEFAULT }

    fun getErrorStatus(edict: Edict): ErrorStatus {
        return when {
            edict.activity == "" -> ErrorStatus.EMPTY_ACTIVITY
            edict.activity == "do something" -> ErrorStatus.ACTIVITY_STILL_DEFAULT
            edict.detailMinutes != null && edict.detailMinutes2 != null && edict.detailMinutes!! >= edict.detailMinutes2!! -> EdictHelper.ErrorStatus.BEFORE_NOT_BEFORE_AFTER
            edict.whileText == "doing something else" -> ErrorStatus.WHILE_STILL_DEFAULT
            edict.whenText == "something else happens" -> ErrorStatus.WHEN_STILL_DEFAULT
            else -> ErrorStatus.READY
        }
    }

    fun getEdictType(edict: Edict): EdictType {
        return when (edict.type) {
            "Never" -> when (edict.detailType) {
                "after" -> EdictType.NEVER_AFTER
                "at" -> EdictType.NEVER_AT
                "before" -> EdictType.NEVER_BEFORE
                "between" -> EdictType.NEVER_BETWEEN
                "ever" -> EdictType.NEVER_EVER
                "when" -> EdictType.NEVER_WHEN
                "while" -> EdictType.NEVER_WHILE
                "on" -> when {
                    edict.onDaysOfTheWeek != null -> EdictType.NEVER_ON_DAYS
                    edict.onText != null -> EdictType.NEVER_ON
                    else -> EdictType.ERROR
                }
                else -> EdictType.ERROR
            }
            "Only" -> when (edict.detailType) {
                "after" -> EdictType.ONLY_AFTER
                "at" -> EdictType.ONLY_AT
                "before" -> EdictType.ONLY_BEFORE
                "between" -> EdictType.ONLY_BETWEEN
                "when" -> EdictType.ONLY_WHEN
                "while" -> EdictType.ONLY_WHILE
                "on" -> when {
                    edict.onDaysOfTheWeek != null -> EdictType.ONLY_ON_DAYS
                    edict.onText != null -> EdictType.ONLY_ON
                    else -> EdictType.ERROR
                }
                else -> EdictType.ERROR
            }
            "Routine:" -> when (edict.detailType) {
                "every" -> when (edict.everyType) {
                    "day" -> EdictType.DO_EVERY_DAY
                    "morning" -> EdictType.DO_EVERY_MORNING
                    "night" -> EdictType.DO_EVERY_NIGHT
                    "#" -> when (edict.numberTimesPerTime) {
                        "days" -> EdictType.DO_EVERY_NUM_DAYS
                        "weeks" -> EdictType.DO_EVERY_NUM_WEEKS
                         else -> EdictType.ERROR
                    }
                    "week" -> EdictType.DO_EVERY_WEEK
                    "time" -> EdictType.DO_EVERY_TIME
                    else -> EdictType.ERROR
                }
                "on" -> when {
                    edict.onDaysOfTheWeek != null -> EdictType.DO_ON_DAYS
                    edict.onText != null -> EdictType.DO_ON
                    else -> EdictType.ERROR
                }
                "#" -> when (edict.numberTimesPerTime) {
                    "day" -> EdictType.DO_NUM_TIMES_PER_DAY
                    "week" -> EdictType.DO_NUM_TIMES_PER_WEEK
                    else -> EdictType.ERROR
                }
                else -> EdictType.ERROR
            }
            else -> EdictType.ERROR
        }
    }
}

