package com.willlockwood.edict.viewmodel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.utils.LevelHelper
import com.willlockwood.edict.utils.TimeHelper

class EdictFragmentVM(
    private val edict: Edict,
    private val sessions: List<EdictSession>
) : BaseObservable() {

    @Bindable
    fun getDeadlineString(): String {
        val timeString = TimeHelper.minutesToTimeString(edict.deadlineMinutes)
        return when (edict.deadlineType) {
            "morning" -> "In the morning ($timeString)"
            "mid-day" -> "In the middle of the day ($timeString)"
            "evening" -> "In the evening ($timeString)"
            "at" -> "At $timeString"
            else -> "${edict.deadlineMinutes} ${edict.deadlineType}"
        }
    }

    @Bindable
    fun getSessions(): String { return sessions.toString() }

    @Bindable
    fun getLevel(): Int { return edict.level }

    @Bindable
    fun getNotifyString(): String {
        val start = TimeHelper.minutesToTimeString(edict.notifyStartMinutes)
        val end = TimeHelper.minutesToTimeString(edict.notifyEndMinutes)
        val at = TimeHelper.minutesToTimeString(edict.notifyAtMinutes)
        return when (start) {
            "" -> when (end) {
                "" -> when (at) {
                    "" -> "None"
                    else -> "At $at" }
                else -> when (at) {
                    "" -> "When it ends ($end)"
                    else -> "When it ends ($end), and at $at" }
            }
            else -> when (end) {
                "" -> when (at) {
                    "" -> "When it starts ($start)"
                    else -> "When it starts ($start), and at $at"
                }
                else -> when (at) {
                    "" -> "When it starts and ends (at $start and $end)"
                    else -> "When it starts ($start), ends ($end), and at $at"
                }
            }
        }
    }

    @Bindable
    fun getNotifyStart(): String { return TimeHelper.minutesToTimeString(edict.notifyStartMinutes) }

    @Bindable
    fun getNotifyEnd(): String { return TimeHelper.minutesToTimeString(edict.notifyEndMinutes) }

    @Bindable
    fun getNotifyAt(): String { return TimeHelper.minutesToTimeString(edict.notifyAtMinutes) }

    @Bindable
    fun getStreakString(): String { return edict.currentStreak.toString() }

    @Bindable
    fun getLevelStreakMax(): Int { return LevelHelper.getLevelStreakMax(edict.level) }

    @Bindable
    fun getLevelStreakMin(): Int { return LevelHelper.getLevelStreakMin(edict.level) }

    @Bindable
    fun getLevelStreakMinString(): String { return getLevelStreakMin().toString() }

    @Bindable
    fun getLevelStreakMaxString(): String { return getLevelStreakMax().toString() }

    @Bindable
    fun getEdictStreak(): Int { return edict.currentStreak }

    @Bindable
    fun getLevelProgress(): Int { return edict.currentStreak - LevelHelper.getLevelStreakMin(edict.level) }

    @Bindable
    fun getEdict(): Edict { return edict }

}