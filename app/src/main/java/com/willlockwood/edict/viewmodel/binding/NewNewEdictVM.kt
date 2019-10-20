package com.willlockwood.edict.viewmodel.binding

import android.content.SharedPreferences
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.utils.TimeHelper
import java.util.*

class NewNewEdictVM(
    private var newEdict: NewEdict
) : BaseObservable() {

    private var liveEdictType = MutableLiveData<NewEdict.Type>()
    private var liveEdictScope = MutableLiveData<NewEdict.Scope>()
    private var liveEdictScalable = MutableLiveData<Boolean>()

    @Bindable
    fun getScope(): NewEdict.Scope? { return newEdict.scope }
    fun getLiveScope(): LiveData<NewEdict.Scope?> = liveEdictScope
    fun setScope(scope: NewEdict.Scope) {
        newEdict.scope = scope
        liveEdictScope.value = null
        notifyPropertyChanged(BR.scope)
        liveEdictScope.value = scope
        notifyPropertyChanged(BR.scope)
        notifyPropertyChanged(BR.newEdict)
    }
    @Bindable
    fun getScopeExampleTexts(): List<String> {
        return listOf(
            when (newEdict.scope) {
                NewEdict.Scope.DAILY -> "Every day,"
                NewEdict.Scope.WEEKLY -> "Every week,"
                NewEdict.Scope.SOME_DAYS -> "On ..."
                NewEdict.Scope.VAR_DAYS -> "Every ... days"
                null -> "..."
            },
            "...",
            "..."
        )
    }

    @Bindable
    fun getDaysExampleTexts(): List<String> {
        return listOf(
            when (newEdict.scope) {
                NewEdict.Scope.SOME_DAYS -> when (newEdict.days) {
                    null -> "On ${when (newEdict.daysText) {
                        null -> "..."
                        else -> newEdict.daysText
                    }},"
                    else -> "On ${newEdict.days!!.joinToString(", ") {
                        when (it) {
                            1 -> "S"
                            2 -> "M"
                            3 -> "T"
                            4 -> "W"
                            5 -> "Th"
                            6 -> "F"
                            7 -> "Su"
                            else -> ""
                        }
                    }},"
                }
                NewEdict.Scope.VAR_DAYS -> "Every ${when (newEdict.daysInt) {
                    null -> "..."
                    else -> newEdict.daysInt.toString()
                } } days,"
                null -> "..."
                else -> "..."
            },
            "...",
            "..."
        )
    }
    @Bindable
    fun getDays(): List<Int>? {
        return when (newEdict.days) {
            null -> emptyList()
            else -> newEdict.days
        }
    }
    fun setDays(day: Int) {
        if (newEdict.days == null) {
            newEdict.days = listOf(day)
            notifyPropertyChanged(BR.days)
        } else if (!newEdict.days!!.contains(day)) {
            newEdict.days = newEdict.days!!.plus(day).sorted()
            notifyPropertyChanged(BR.days)
        } else {
            newEdict.days = newEdict.days!!.filter { it != day }.sorted()
            notifyPropertyChanged(BR.days)
        }
        if (newEdict.days != null && newEdict.daysText != null) {
            newEdict.daysText = null
            notifyPropertyChanged(BR.daysText)
        }
        notifyPropertyChanged(BR.daysCanContinue)
        notifyPropertyChanged(BR.daysExampleTexts)
        notifyPropertyChanged(BR.newEdict)
    }

    @Bindable
    fun getDaysText(): String? {
        return when (newEdict.daysText) {
            null -> ""
            else -> newEdict.daysText
        }
    }
    fun setDaysText(text: String) {
        val textValue = when (text) {
            "" -> null
            else -> text
        }
        if (newEdict.daysText != textValue) {
            newEdict.daysText = textValue
            if (textValue != null && newEdict.days != null) {
                newEdict.days = null
                notifyPropertyChanged(BR.days)
            }
            notifyPropertyChanged(BR.daysCanContinue)
            notifyPropertyChanged(BR.daysExampleTexts)
            notifyPropertyChanged(BR.newEdict)
        }
    }
    @Bindable
    fun getDaysInt(): String? {
        return when (newEdict.daysInt) {
            null -> ""
            else -> newEdict.daysInt.toString()
        }
    }
    fun setDaysInt(text: String) {
        val textValue = when (text) {
            "" -> null
            else -> text.toInt()
        }
        if (newEdict.daysInt != textValue) {
            newEdict.daysInt = textValue
            notifyPropertyChanged(BR.daysInt)
            notifyPropertyChanged(BR.daysCanContinue)
            notifyPropertyChanged(BR.daysExampleTexts)
            notifyPropertyChanged(BR.newEdict)

        }
    }
    @Bindable
    fun getDaysCanContinue(): Boolean {
        return when (newEdict.scope!!) {
            NewEdict.Scope.SOME_DAYS -> when (newEdict.days) {
                null -> when (newEdict.daysText) {
                    null -> false
                    "" -> false
                    else -> true
                }
                else -> true
            }
            NewEdict.Scope.VAR_DAYS -> when (newEdict.daysInt) {
                null -> false
                1 -> false
                else -> true
            }
            else -> false
        }
    }



    @Bindable
    fun getType(): NewEdict.Type? { return newEdict.type }
    fun getLiveType(): LiveData<NewEdict.Type?> = liveEdictType
    fun setType(type: NewEdict.Type) {
        newEdict.type = type
        liveEdictType.value = null
        notifyPropertyChanged(BR.type)
        liveEdictType.value = type
        notifyPropertyChanged(BR.type)
        notifyPropertyChanged(BR.newEdict)
    }

    @Bindable
    fun getScalable(): Boolean? { return newEdict.scalable }
    fun getLiveScalable(): LiveData<Boolean?> = liveEdictScalable
    fun setScalable(scalable: Boolean) {
        newEdict.scalable = scalable
        liveEdictScalable.value = null
        notifyPropertyChanged(BR.scalable)
        liveEdictScalable.value = scalable
        notifyPropertyChanged(BR.scalable)
        notifyPropertyChanged(BR.newEdict)
    }
    @Bindable
    fun getNotScalable(): Boolean? {
        return when (newEdict.scalable) {
            null -> null
            false -> true
            true -> false
        }
    }
    private var scalableNum = 1
    @Bindable
    fun getScalableExampleList(): List<String> {
        val numTimes = when (newEdict.type) {
            NewEdict.Type.RESTRICTION -> 5 - scalableNum
            else -> scalableNum
        }
        val sIfPlural = when (numTimes) {
            1 -> ""
            else -> "s"
        }
        return when (newEdict.type) {
            NewEdict.Type.RESTRICTION -> listOf("\"only smoke $numTimes time$sIfPlural\"", "\"only smoke twice\"")
                NewEdict.Type.ROUTINE -> listOf("\"work out $numTimes time$sIfPlural\"", "\"work out once\"")
                null -> listOf("", "")
        }
    }
    fun setScalableNum(num: Int) {
        if (this.scalableNum != num) {
            this.scalableNum = num
            notifyPropertyChanged(BR.scalableExampleList)
            notifyPropertyChanged(BR.newEdict)
        }
    }


    @Bindable
    fun getRuleCurrentValuesList(): List<String> {
        val daysText = when (newEdict.scope) {
            NewEdict.Scope.DAILY -> when (newEdict.type!!) {
                NewEdict.Type.ROUTINE -> "Every day,"
                NewEdict.Type.RESTRICTION -> when (newEdict.scalable!!) {
                    true -> "Each day,"
                    false -> ""
                }
            }
            NewEdict.Scope.WEEKLY -> when (newEdict.type!!) {
                NewEdict.Type.ROUTINE -> "Every week,"
                NewEdict.Type.RESTRICTION -> when (newEdict.scalable!!) {
                    true -> "Each week,"
                    false -> ""
                }
            }
            NewEdict.Scope.SOME_DAYS -> "On ___"
            NewEdict.Scope.VAR_DAYS -> "Every x days"
            null -> ""
        }

        val daysHint = when (newEdict.scope) {
            NewEdict.Scope.DAILY -> "(every day)"
            NewEdict.Scope.WEEKLY -> "(every week)"
            NewEdict.Scope.SOME_DAYS -> "(on x, y, and z,)"
            NewEdict.Scope.VAR_DAYS -> "(every x days)"
            null -> ""
        }

        val ruleHint = when (newEdict.scalable!!) {
            true -> getCurrentRuleExampleList().joinToString(" ")
            false -> getCurrentRuleExampleList()[0]
        }

        val actionText = when (newEdict.action) {
            null -> ""
            else -> newEdict.action
        }
        val unitVar = when (newEdict.unitVar) {
            null -> ""
            else -> "${newEdict.unitVar}"
        }
        val unit = when (newEdict.unit) {
            null -> ""
            else -> "${newEdict.unit}"
        }
        val ruleText = when (newEdict.scalable) {
            true -> "$actionText $unitVar $unit"
            false -> "$actionText"
            null -> ""
        }
        notifyPropertyChanged(BR.ruleCurrentValuesNotEmpty)
        return listOf(daysHint, daysText, ruleHint, ruleText)
    }

    @Bindable
    fun getRuleCurrentValuesNotEmpty(): List<Boolean> {
        val valuesList = getRuleCurrentValuesList()
        return valuesList.map { it.replace(" ", "") != "" }
    }

    @Bindable
    fun getRuleAction(): String? {
        return when(newEdict.action) {
            null -> ""
            else -> newEdict.action
        }
    }
    fun setRuleAction(actionText: String) {
        if (newEdict.action != actionText) {
            newEdict.action = actionText
            notifyPropertyChanged(BR.ruleWarningShown)
            notifyPropertyChanged(BR.ruleAction)
            notifyPropertyChanged(BR.ruleCurrentValuesList)
            notifyPropertyChanged(BR.ruleCanContinue)
            notifyPropertyChanged(BR.newEdict)
        }
    }

    @Bindable
    fun getRuleVar(): String? {
        return when (newEdict.unitVar) {
            null -> ""
            else -> newEdict.unitVar.toString()
        }
    }
    fun setRuleVar(value: String) {
        val ruleInt = when (value) {
            "" -> null
            "null" -> null
            else -> value.toInt()
        }
        if (newEdict.unitVar != ruleInt) {
            newEdict.unitVar = ruleInt
            notifyPropertyChanged(BR.ruleVar)
            notifyPropertyChanged(BR.ruleCurrentValuesList)
            notifyPropertyChanged(BR.ruleCanContinue)
            notifyPropertyChanged(BR.newEdict)
        }
    }

    @Bindable
    fun getRuleUnit(): String? { return newEdict.unit }
    fun setRuleUnit(actionText: String) {
        if (newEdict.unit != actionText) {
            newEdict.unit = actionText
            notifyPropertyChanged(BR.ruleWarningShown)
            notifyPropertyChanged(BR.ruleUnit)
            notifyPropertyChanged(BR.ruleCurrentValuesList)
            notifyPropertyChanged(BR.ruleCanContinue)
            notifyPropertyChanged(BR.newEdict)
        }
    }

    @Bindable
    fun getRuleWarningShown(): Boolean {
        val warningStrings = listOf(" per ", " every ", " after", " before", " on ", " each ", " between", " at")
        val combinedString = "${newEdict.action} ${newEdict.unit}"
        for (word in warningStrings) {
            if (combinedString.contains(word)) {
                return true
            }
        }
        return false
    }

    @Bindable
    fun getRuleCanContinue(): Boolean {
        return when (newEdict.scalable!!) {
            true -> when {
                newEdict.action == null || newEdict.action == "" -> false
                newEdict.unitVar == null -> false
                newEdict.unit == null || newEdict.unit == "" -> false
                else -> true
            }
            false -> when {
                newEdict.action == null || newEdict.action == "" -> false
                else -> true
            }
        }
    }

    private var timeTypeValues = NewEdict.TimeType.values().map { it.name
        .toLowerCase(Locale.ENGLISH)
        .replace("_", " ")
        .replace(" time", "")
        .replace(" text", "...")
    }.toTypedArray()

    @Bindable
    fun getTimeTypeValues(): Array<String> {
        return timeTypeValues
    }

    private var timeTypeIdx = 0
    @Bindable
    fun getTimeTypeIdx(): Int { return timeTypeIdx }
    fun setTimeTypeIdx(value: Int) {
        this.timeTypeIdx = value
        setTimeType(timeTypeValues[value])
        notifyPropertyChanged(BR.timeTypeIdx)
    }

    @Bindable
    fun getTimeTextHint(): String? {
        return when (newEdict.timeType) {
            NewEdict.TimeType.ALL_DAY -> null
            NewEdict.TimeType.AT -> "some location"
            NewEdict.TimeType.AFTER_TIME -> null
            NewEdict.TimeType.AFTER_TEXT -> "doing something else"
            NewEdict.TimeType.BEFORE_TIME -> null
            NewEdict.TimeType.BEFORE_TEXT -> "doing something else"
            NewEdict.TimeType.BETWEEN_TIME -> null
            NewEdict.TimeType.BETWEEN_TEXT -> "two other events"
            NewEdict.TimeType.WHEN -> "something else happens"
            NewEdict.TimeType.WHILE -> "doing something else"
            null -> null
        }
    }

    @Bindable
    fun getTimeType(): NewEdict.TimeType? { return newEdict.timeType }
    fun setTimeType(value: String) {
        val timeType = when (value) {
            "all day" -> NewEdict.TimeType.ALL_DAY
            "at" -> NewEdict.TimeType.AT
            "after" -> NewEdict.TimeType.AFTER_TIME
            "after..." -> NewEdict.TimeType.AFTER_TEXT
            "before" -> NewEdict.TimeType.BEFORE_TIME
            "before..." -> NewEdict.TimeType.BEFORE_TEXT
            "between" -> NewEdict.TimeType.BETWEEN_TIME
            "between..." -> NewEdict.TimeType.BETWEEN_TEXT
            "when" -> NewEdict.TimeType.WHEN
            "while" -> NewEdict.TimeType.WHILE
            else -> null
        }
        if (newEdict.timeType != timeType) {
            when (timeType) {
                NewEdict.TimeType.AFTER_TIME -> {
                    setTimeStart(300)
                    setTimeEnd(1440)
                    setTimeText(null)
                }
                NewEdict.TimeType.BEFORE_TIME -> {
                    setTimeStart(0)
                    setTimeEnd(800)
                    setTimeText(null)
                }
                NewEdict.TimeType.BETWEEN_TIME -> {
                    setTimeStart(300)
                    setTimeEnd(800)
                    setTimeText(null)
                }
                else -> {
                    setTimeStart(0)
                    setTimeEnd(1440)
                    setTimeText(null)
                }
            }
            newEdict.timeType = timeType
            notifyPropertyChanged(BR.timeType)
            notifyPropertyChanged(BR.timeTextHint)
            notifyPropertyChanged(BR.newEdict)
        }
    }

//    @Bindable
//    fun getCheckInStartPercent(): Float? { return getCheckInStartMinutes().toFloat() / 1440 }
//    @Bindable
//    fun getCheckInEndPercent(): Float? { return getCheckInEndMinutes().toFloat() / 1440 }

    @Bindable
    fun getTimeStart(): String? { return TimeHelper.minutesToTimeString(newEdict.timeStart) }
    fun setTimeStart(minutes: Int) {
        if (newEdict.timeStart != minutes) {
            newEdict.timeStart = minutes
            notifyPropertyChanged(BR.timeStart)
            notifyPropertyChanged(BR.newEdict)
        }
//        notifyPropertyChanged(BR.startPercent)
    }

    @Bindable
    fun getTimeEnd(): String? { return TimeHelper.minutesToTimeString(newEdict.timeEnd) }
    fun setTimeEnd(minutes: Int) {
        if (newEdict.timeEnd != minutes) {
            newEdict.timeEnd = minutes
            notifyPropertyChanged(BR.timeEnd)
            notifyPropertyChanged(BR.newEdict)
        }
//        notifyPropertyChanged(BR.endPercent)
    }

    @Bindable
    fun getTimeText(): String? { return newEdict.timeText }
    fun setTimeText(text: String?) {
        val newText = when (text) {
            "" -> null
            else -> text
        }
        if (newEdict.timeText != newText) {
            newEdict.timeText = newText
            notifyPropertyChanged(BR.timeText)
            notifyPropertyChanged(BR.newEdict)
        }
    }

    @Bindable
    fun getCheckInStart(): String { return TimeHelper.minutesToTimeString(newEdict.checkInStart) }
    fun setCheckInStart(time: String) {
        val minutes = TimeHelper.getMinutesFromTimeString(time)
        if (newEdict.checkInStart != minutes) {
            newEdict.checkInStart = minutes
            notifyPropertyChanged(BR.checkInStart)
        }
    }
    fun updateNullCheckInStart() {
        if (newEdict.checkInStart == null) {
            newEdict.checkInStart = newEdict.timeStart
            notifyPropertyChanged(BR.checkInStart)
        }
    }

    @Bindable
    fun getCheckInEnd(): String { return TimeHelper.minutesToTimeString(newEdict.checkInEnd) }
    fun setCheckInEnd(time: String) {
        val minutes = TimeHelper.getMinutesFromTimeString(time)
        if (newEdict.checkInEnd != minutes) {
            newEdict.checkInEnd = minutes
            notifyPropertyChanged(BR.checkInEnd)
        }
    }
    fun updateNullCheckInEnd() {
        if (newEdict.checkInEnd == null) {
            newEdict.checkInEnd = newEdict.timeEnd
            notifyPropertyChanged(BR.checkInEnd)
        }
    }


    @Bindable
    fun getCheckInWindowText(): String {
        val startTime = getCheckInStart().replace(" ", "").replace(":00", "").toLowerCase()
        val endTime = getCheckInEnd().replace(" ", "").replace(":00", "").toLowerCase()
        return "$startTime-$endTime"
    }

    @Bindable
    fun getScopeExampleList(): List<String> {
        return when (newEdict.type) {
            NewEdict.Type.RESTRICTION -> listOf(
                "Drink at most 1 soda every day",
                "Only drink 5 sodas per week",
                "Don't drink sodas on weekdays",
                "Only drink 2 sodas every 3 days"
            )
            NewEdict.Type.ROUTINE -> listOf(
                "Work out every day",
                "Work out 4 times per week",
                "Work out on Mon. and Fri.",
                "Work out once every 3 days"
            )
            else -> listOf(
                "Every day, ...",
                "Every week, ...",
                "On Monday and Friday, ...",
                "Every 3 days, ..."
            )
        }
    }

    private lateinit var sharedPreferences: SharedPreferences
    fun setSharedPreferences(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    var currentExamplePosition = 0
    @Bindable
    fun getCurrentRuleExampleList(): List<String> {
        return getRuleExampleLists()[currentExamplePosition % 3]
    }
    fun getNextRuleExample() {
        currentExamplePosition += 1
        notifyPropertyChanged(BR.currentRuleExampleList)
    }

    @Bindable
    fun getRuleExampleLists(): List<List<String>> {
        return when (newEdict.scalable!!) {
            true -> when (newEdict.type!!) {
                NewEdict.Type.RESTRICTION -> listOf(
                    listOf("Only smoke", "3", "cigarettes"),
                    listOf("Eat less than", "2000", "calories"),
                    listOf("Check emails up to", "5", "times")
                )
                NewEdict.Type.ROUTINE -> listOf(
                    listOf("Meditate for", "30", "minutes"),
                    listOf("Cook dinner", "2", "times"),
                    listOf("Compliment", "3", "people")
                )
            }
            false -> when (newEdict.type!!) {
                NewEdict.Type.RESTRICTION -> listOf(
                    listOf("Don't drink alone", "", ""),
                    listOf("No eating bagels", "", ""),
                    listOf("Only check Twitter", "", "")
                )
                NewEdict.Type.ROUTINE -> listOf(
                    listOf("Walk the dogs", "", ""),
                    listOf("Read a book", "", ""),
                    listOf("Work out", "", "")
                )
            }
        }
    }



    @Bindable
    fun getRuleExampleHintLists(): String {
        return when (newEdict.scalable!!) {
            true -> when (newEdict.type!!) {
                NewEdict.Type.RESTRICTION -> "Only smoke 3 cigarettes"
                NewEdict.Type.ROUTINE -> "Meditate for 30 minutes"
            }
            false -> when (newEdict.type!!) {
                NewEdict.Type.RESTRICTION -> "Don't drink alone"
                NewEdict.Type.ROUTINE -> "Walk the dogs"
            }
        }
    }

    @Bindable
    fun getReviewDeadlineDetails(): String {
        val checkInStart = getCheckInStart()
        val checkInEnd = getCheckInEnd()
        return "Between $checkInStart and $checkInEnd"
    }

    @Bindable
    fun getReviewRuleText(): String { return newEdict.ruleString() }

    @Bindable
    fun getReviewType(): String {
        return when (newEdict.type) {
            NewEdict.Type.RESTRICTION -> "Restriction"
            NewEdict.Type.ROUTINE -> "Routine"
            null -> ""
        }
    }

    @Bindable
    fun getReviewRuleHeader(): String {
        return when (newEdict.type) {
            NewEdict.Type.RESTRICTION -> "Restriction action"
            NewEdict.Type.ROUTINE -> "Routine action"
            null -> ""
        }
    }

    @Bindable
    fun getReviewTimeDetails(): String {
        return when (newEdict.timeType) {
            NewEdict.TimeType.ALL_DAY -> "All day"
            NewEdict.TimeType.AFTER_TIME -> "After ${TimeHelper.minutesToTimeString(newEdict.timeStart)}"
            NewEdict.TimeType.AFTER_TEXT -> "After ${newEdict.timeText}"
            NewEdict.TimeType.BEFORE_TIME -> "Before ${TimeHelper.minutesToTimeString(newEdict.timeEnd)}"
            NewEdict.TimeType.BEFORE_TEXT -> "Before ${newEdict.timeText}"
            NewEdict.TimeType.BETWEEN_TIME -> "Between ${TimeHelper.minutesToTimeString(newEdict.timeStart)} and ${TimeHelper.minutesToTimeString(newEdict.timeEnd)}"
            NewEdict.TimeType.BETWEEN_TEXT -> "Between ${newEdict.timeText}"
            NewEdict.TimeType.WHEN -> "When ${newEdict.timeText}"
            NewEdict.TimeType.WHILE -> "While ${newEdict.timeText}"
            NewEdict.TimeType.AT -> "At ${newEdict.timeText}"
            null -> ""
        }
    }

    @Bindable
    fun getReviewActiveDays(): String {
        return when (newEdict.scope) {
            NewEdict.Scope.DAILY -> "Every day"
            NewEdict.Scope.WEEKLY -> "Every week"
            NewEdict.Scope.SOME_DAYS -> {
                val text = newEdict.daysText
                when (newEdict.daysText) {
                    null -> newEdict.days!!.map {
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
                    }.joinToString(", ")
                    else -> text!!
                }
            }
            NewEdict.Scope.VAR_DAYS -> ""
            null -> ""
        }
    }

    @Bindable
    fun getReviewNotifications(): String {
        return newEdict.notificationsToString(newEdict.notifications)
    }

    @Bindable
    fun getCheckInCanContinue(): Boolean {
        val start = newEdict.checkInStart
        val end = newEdict.checkInEnd
        return end != null && start != null && end > start
    }

    @Bindable
    fun getEdictStartTimeShort(): String { return "(${TimeHelper.minutesToTimeStringShort(newEdict.timeStart)})" }
    @Bindable
    fun getEdictEndTimeShort(): String { return "(${TimeHelper.minutesToTimeStringShort(newEdict.timeEnd)})" }

    @Bindable
    fun getCheckInStartTimeShort(): String { return "(${TimeHelper.minutesToTimeStringShort(newEdict.checkInStart)})" }

    @Bindable
    fun getEdictStartReminderVisible(): Boolean {
        return when (newEdict.timeType) {
            NewEdict.TimeType.AFTER_TIME -> true
            NewEdict.TimeType.BETWEEN_TIME -> true
            else -> false
        }
    }
    @Bindable
    fun getEdictEndReminderVisible(): Boolean {
        return when (newEdict.timeType) {
            NewEdict.TimeType.BEFORE_TIME -> true
            NewEdict.TimeType.BETWEEN_TIME -> true
            else -> false
        }
    }

    var remindAtMinutes = 600
    @Bindable
    fun getCheckInEndTimeShort(): String { return "(${TimeHelper.minutesToTimeStringShort(newEdict.checkInEnd)})" }
    fun addNotification(type: NewEdict.NotificationType, minutes: Int? = null) {
        if (newEdict.hasNotification(type)) {
            newEdict.removeNotification(type)
        } else {

            newEdict.addToNotifications(type, minutes)
        }
        notifyPropertyChanged(BR.newEdict)
    }


    @Bindable
    fun getNewEdict(): NewEdict { return this.newEdict }
    fun setNewEdict(newEdict: NewEdict) {
        if (newEdict != this.newEdict) {
            this.newEdict = newEdict
            notifyPropertyChanged(BR.newEdict)

        }
    }

    fun populateFieldsForFragment(fragment: NewEdictFragment) {
        when (fragment) {
            NewEdictFragment.INTRO -> {}
            NewEdictFragment.SCOPE -> {
                notifyPropertyChanged(BR.scope)
                notifyPropertyChanged(BR.scopeExampleList)
            }
            NewEdictFragment.DAYS -> {
                notifyPropertyChanged(BR.days)
                notifyPropertyChanged(BR.daysText)
            }
            NewEdictFragment.TYPE -> {
                notifyPropertyChanged(BR.type)
                notifyPropertyChanged(BR.daysExampleTexts)
            }
            NewEdictFragment.SCALE -> {
                notifyPropertyChanged(BR.scalable)
                notifyPropertyChanged(BR.notScalable)
            }
            NewEdictFragment.TEXT -> {
                notifyPropertyChanged(BR.ruleExampleLists)
                notifyPropertyChanged(BR.ruleAction)
                notifyPropertyChanged(BR.ruleVar)
                notifyPropertyChanged(BR.ruleUnit)
                notifyPropertyChanged(BR.ruleWarningShown)
            }
            NewEdictFragment.TIME -> {
                notifyPropertyChanged(BR.timeTextHint)
                notifyPropertyChanged(BR.timeText)
                notifyPropertyChanged(BR.timeEnd)
                notifyPropertyChanged(BR.timeStart)
                notifyPropertyChanged(BR.timeType)
                notifyPropertyChanged(BR.timeTypeIdx)
//                notifyPropertyChanged(BR.endPercent)
//                notifyPropertyChanged(BR.startPercent)
            }
            NewEdictFragment.DEADLINE -> {
                notifyPropertyChanged(BR.checkInStart)
                notifyPropertyChanged(BR.checkInEnd)
                notifyPropertyChanged(BR.checkInCanContinue)
            }
        }
    }

    enum class NewEdictFragment {
        INTRO, SCOPE, DAYS, TYPE, SCALE, TEXT, TIME, DEADLINE
    }

}