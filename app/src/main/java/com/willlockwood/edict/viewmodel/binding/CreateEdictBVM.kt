package com.willlockwood.edict.viewmodel.binding

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.MutableLiveData
import com.willlockwood.edict.data.model.NewEdict
import com.willlockwood.edict.fragment.CreateEdict
import com.willlockwood.edict.utils.TimeHelper
import org.threeten.bp.DayOfWeek

class CreateEdictBVM(
    private var newEdict: NewEdict,
    private var listener: ViewListener
) : BaseObservable() {

    interface ViewListener {
        fun openDialog(type: CreateEdict.DialogType, scalable: Boolean? = null)
    }

    private var edictHasError = MutableLiveData<Boolean?>()

    fun openDialog(type: CreateEdict.DialogType) {
        when (type) {
            CreateEdict.DialogType.DAYS -> listener.openDialog(type)
            CreateEdict.DialogType.ACTION -> listener.openDialog(type, getScalable())
            CreateEdict.DialogType.TIMES -> listener.openDialog(type)
        }
    }

    fun openDialog(type: CreateEdict.DialogType, checked: Boolean) {
        when (type) {
            CreateEdict.DialogType.DAYS -> listener.openDialog(type)
            CreateEdict.DialogType.ACTION -> listener.openDialog(type, !checked)
            CreateEdict.DialogType.TIMES -> listener.openDialog(type)
        }
    }

    init {
        if (newEdict.type == null)      newEdict.type = NewEdict.Type.ROUTINE
        if (newEdict.scalable == null)  setScalable(true)
        if (newEdict.scope == null)     setScope(NewEdict.Scope.DAILY)

        setCheckInStart(1200)
        setCheckInEnd(1320)
        updateEdictError()
    }

    @Bindable
    fun getNewEdict(): NewEdict {
        notifyPropertyChanged(BR.fullText)
        return newEdict
    }
    fun setNewEdict(newEdict: NewEdict) {
        this.newEdict = newEdict
        notifyPropertyChanged(BR.newEdict)
    }

    @Bindable
    fun getScope(): NewEdict.Scope? { return newEdict.scope }
    fun setScope(value: NewEdict.Scope) {
        if (newEdict.scope != value) {
            val oldScope = newEdict.scope
            newEdict.scope = value
            when (oldScope) {
                NewEdict.Scope.SOME_DAYS -> newEdict.days = emptyList()
                NewEdict.Scope.TEXT_DAYS -> newEdict.daysText = null
                NewEdict.Scope.VAR_DAYS -> newEdict.daysInt = null
                else -> {}
            }
            notifyPropertyChanged(BR.scope)
            notifyPropertyChanged(BR.daysSubheader)
            notifyPropertyChanged(BR.newEdict)
        }
    }

    fun toggleDay(value: DayOfWeek) {
        newEdict.toggleDayOfWeek(value)
        notifyPropertyChanged(BR.newEdict)
        notifyPropertyChanged(BR.daysSubheader)
    }

    @Bindable
    fun getType(): NewEdict.Type { return newEdict.type!! }

    @Bindable
    fun getDaysText(): String {
        return when (newEdict.daysText) {
            null -> ""
            else -> newEdict.daysText!!
        }
    }
    fun setDaysText(value: String) {
        val text = when (value) {
            "" -> null
            else -> value
        }
        if (newEdict.daysText != text) {
            newEdict.daysText = text
            notifyPropertyChanged(BR.daysText)
            notifyPropertyChanged(BR.daysSubheader)
            notifyPropertyChanged(BR.newEdict)
        }
    }

    @Bindable
    fun getDaysInt(): String {
        return when (newEdict.daysInt) {
            null -> ""
            else -> newEdict.daysInt.toString()
        }
    }
    fun setDaysInt(value: String) {
        val number = when (value) {
            "" -> null
            else -> value.toInt()
        }
        if (newEdict.daysInt != number) {
            newEdict.daysInt = number
            notifyPropertyChanged(BR.daysInt)
            notifyPropertyChanged(BR.daysSubheader)
            notifyPropertyChanged(BR.daysSubheaderError)
            updateEdictError()
            notifyPropertyChanged(BR.newEdict)
        }
    }

    fun updateEdictError() {
        val error: Boolean = getDaysSubheaderError() || getActionSubheaderError() || getTimesSubheaderError()
        if (edictHasError.value != error) {
            edictHasError.value = error
        }

    }

    @Bindable
    fun getDaysSubheader(): String {
        notifyPropertyChanged(BR.daysSubheaderError)
        return newEdict.getDaysSubheader()
    }
    @Bindable
    fun getDaysSubheaderError(): Boolean {
        if ( !doneFabClicked ) { return false }
        return when (newEdict.scope) {
            NewEdict.Scope.SOME_DAYS -> newEdict.days.isEmpty()
            NewEdict.Scope.TEXT_DAYS -> newEdict.daysText == null
            NewEdict.Scope.VAR_DAYS -> newEdict.daysInt == null
            else -> false
        }
    }
    @Bindable
    fun getActionHeader(): String { return newEdict.getActionHeader() }
    @Bindable
    fun getActionSubheader(): String { return newEdict.getActionSubheader() }
    @Bindable
    fun getActionSubheaderError(): Boolean {
        if ( !doneFabClicked ) { return false }
        return when (newEdict.scalable) {
            true -> newEdict.action == null || newEdict.unitVar == null || newEdict.unit == null
            false -> newEdict.action == null
            else -> true
        }
    }
    @Bindable
    fun getTimeSubheader(): String { return newEdict.getTimesSubheader() }
    @Bindable
    fun getTimesSubheaderError(): Boolean {
        if ( !doneFabClicked ) { return false }
        return when (newEdict.timeType) {
            NewEdict.TimeType.ALL_DAY -> false
            NewEdict.TimeType.AFTER_TIME -> newEdict.timeStart == null
            NewEdict.TimeType.BEFORE_TIME -> newEdict.timeEnd == null
            NewEdict.TimeType.BETWEEN_TIME -> newEdict.timeStart == null || newEdict.timeEnd == null
            else -> newEdict.timeText == null
        }
    }

    @Bindable
    fun getAction(): String { return newEdict.ruleString() }
    fun setAction(action: String, unitVar: Int?, unit: String?) {

        if (newEdict.action != action)      newEdict.action = action
        if (newEdict.unitVar != unitVar)    newEdict.unitVar = unitVar
        if (newEdict.unit != unit)          newEdict.unit = unit

        notifyPropertyChanged(BR.action)
        notifyPropertyChanged(BR.newEdict)
        notifyPropertyChanged(BR.actionHeader)
        notifyPropertyChanged(BR.actionSubheader)
        notifyPropertyChanged(BR.actionSubheaderError)
        updateEdictError()
    }

    @Bindable
    fun getTimeType(): NewEdict.TimeType? { return newEdict.timeType }
    fun setTimeType(value: NewEdict.TimeType?) {
        if (newEdict.timeType != value) {
            newEdict.timeType = value
            setTimeTypeDefaults()
            notifyPropertyChanged(BR.timeType)
            notifyPropertyChanged(BR.timeTypeWord)
            notifyPropertyChanged(BR.timeSubheader)
            notifyPropertyChanged(BR.timesSubheaderError)
            updateEdictError()
            notifyPropertyChanged(BR.newEdict)
        }
    }

    @Bindable
    fun getTimeTypeWord(): String { return when (newEdict.timeType) {
        NewEdict.TimeType.ALL_DAY ->        "All the time"
        NewEdict.TimeType.AFTER_TIME ->     "After"
        NewEdict.TimeType.AFTER_TEXT ->     "After"
        NewEdict.TimeType.BEFORE_TIME ->    "Before"
        NewEdict.TimeType.BEFORE_TEXT ->    "Before"
        NewEdict.TimeType.BETWEEN_TIME ->   "Between"
        NewEdict.TimeType.BETWEEN_TEXT ->   "Between"
        NewEdict.TimeType.WHEN ->           "When"
        NewEdict.TimeType.WHILE ->          "While"
        NewEdict.TimeType.AT ->             "At"
        null -> ""
    }}

    fun setTimeTypeDefaults() {
        when (newEdict.timeType) {
            NewEdict.TimeType.AFTER_TIME ->     setTimes(600, 1440)
            NewEdict.TimeType.BEFORE_TIME ->    setTimes(0, 800)
            NewEdict.TimeType.BETWEEN_TIME ->   setTimes(600, 800)
            null -> {}
            else ->                             setTimeText("")
//            NewEdict.TimeType.ALL_DAY ->        setTimeText("")
//            NewEdict.TimeType.AFTER_TEXT ->     setTimeText("")
//            NewEdict.TimeType.BEFORE_TEXT ->    setTimeText("")
//            NewEdict.TimeType.BETWEEN_TEXT ->   setTimeText("")
//            NewEdict.TimeType.WHEN ->           setTimeText("")
//            NewEdict.TimeType.WHILE ->          setTimeText("")
//            NewEdict.TimeType.AT ->             setTimeText("")
        }
        notifyPropertyChanged(BR.timeTextHint)
    }

    @Bindable
    fun getTimeText(): String? { return newEdict.timeText }
    fun setTimeText(value: String) {
        val text = when (value) {
            "" -> null
            else -> value
        }
        if (newEdict.timeText != text) {
            newEdict.timeText = text
            notifyPropertyChanged(BR.timeText)
            notifyPropertyChanged(BR.timeSubheader)
            notifyPropertyChanged(BR.timesSubheaderError)
            updateEdictError()
            notifyPropertyChanged(BR.newEdict)
        }
        setTimes(0, 1440)
    }

    @Bindable
    fun getTimeTextHint(): String { return when (newEdict.timeType) {
        NewEdict.TimeType.AFTER_TEXT ->     "school"
        NewEdict.TimeType.BEFORE_TEXT ->    "work"
        NewEdict.TimeType.BETWEEN_TEXT ->   "dinner and bedtime"
        NewEdict.TimeType.WHEN ->           "something else happens"
        NewEdict.TimeType.WHILE ->          "doing something else"
        else ->                             ""
    }}

    @Bindable
    fun getTimeStart(): String { return TimeHelper.minutesToTimeStringShort(newEdict.timeStart) }
    fun setTimeStart(value: Int) {
//        if (newEdict.timeStart != value) {
            newEdict.timeStart = value
            notifyPropertyChanged(BR.timeStart)
            notifyPropertyChanged(BR.newEdict)
            notifyPropertyChanged(BR.timeSubheader)
            notifyPropertyChanged(BR.timesSubheaderError)
            updateEdictError()
//        }
    }
    @Bindable
    fun getTimeEnd(): String { return TimeHelper.minutesToTimeStringShort(newEdict.timeEnd) }
    fun setTimeEnd(value: Int) {
//        if (newEdict.timeEnd != value) {
            newEdict.timeEnd = value
            notifyPropertyChanged(BR.timeEnd)
            notifyPropertyChanged(BR.newEdict)
            notifyPropertyChanged(BR.timeSubheader)
            notifyPropertyChanged(BR.timesSubheaderError)
            updateEdictError()
//        }
    }
    fun setTimes(start: Int, end: Int) {
        setTimeStart(start)
        setTimeEnd(end)
    }

    fun getMinutesFromIdx(idx: Int): Int {
        return when (idx) {
            0 -> 15
            1 -> 30
            2 -> 45
            3 -> 60
            4 -> 90
            5 -> 120
            else -> 120
        }
    }

    private var notifyBeforeEnd = 15
    fun setNotifyBeforeEnd(idx: Int) {
        val minutes = getMinutesFromIdx(idx)
        if (notifyBeforeEnd != minutes) {
            notifyBeforeEnd = minutes
            if (newEdict.hasNotification(NewEdict.NotificationType.BEFORE_END)) {
                newEdict.removeNotification(NewEdict.NotificationType.BEFORE_END)
                newEdict.addToNotifications(NewEdict.NotificationType.BEFORE_END, notifyBeforeEnd)
            }
        }
    }
    private var notifyBeforeCheckInEnd = 15
    fun setNotifyBeforeCheckInEnd(idx: Int) {
        val minutes = getMinutesFromIdx(idx)
        if (notifyBeforeCheckInEnd != minutes) {
            notifyBeforeCheckInEnd = minutes
            if (newEdict.hasNotification(NewEdict.NotificationType.CHECK_IN_BEFORE_END)) {
                newEdict.removeNotification(NewEdict.NotificationType.CHECK_IN_BEFORE_END)
                newEdict.addToNotifications(NewEdict.NotificationType.CHECK_IN_BEFORE_END, notifyBeforeCheckInEnd)
            }
        }
    }
    private var notifyAtMinutes = 10
    @Bindable
    fun getNotifyAt(): String { return TimeHelper.minutesToTimeStringShort(notifyAtMinutes) }
    fun setNotifyAt(value: Int) {
        if (notifyAtMinutes != value) {
            notifyAtMinutes = value
            if (newEdict.hasNotification(NewEdict.NotificationType.AT)) {
                addNotification(NewEdict.NotificationType.AT, notifyAtMinutes)
            }
            notifyPropertyChanged(BR.notifyAt)
        }
    }
    fun addNotification(type: NewEdict.NotificationType, minutes: Int?) {
        when (type) {
            NewEdict.NotificationType.AT -> newEdict.addToNotifications(type, notifyAtMinutes)
            NewEdict.NotificationType.BEFORE_END -> newEdict.addToNotifications(type, notifyBeforeEnd)
            NewEdict.NotificationType.CHECK_IN_BEFORE_END -> newEdict.addToNotifications(type, notifyBeforeCheckInEnd)
            else -> newEdict.addToNotifications(type, minutes)
        }
        notifyPropertyChanged(BR.newEdict)
        notifyPropertyChanged(BR.notifHeader)
    }
    fun removeNotification(type: NewEdict.NotificationType) {
        newEdict.removeNotification(type)
        notifyPropertyChanged(BR.newEdict)
        notifyPropertyChanged(BR.notifHeader)
    }
    @Bindable
    fun getNotifHeader(): String { return when (newEdict.notifications.size) {
        1 -> "${newEdict.notifications.size} notification"
        else -> "${newEdict.notifications.size} notifications"
    } }

    private var notifDetailsVisibility: Int = View.GONE
    @Bindable
    fun getNotifDetailsVisibility(): Int { return notifDetailsVisibility }
    fun toggleNotifDetailsVisibility() {
        notifDetailsVisibility = when (notifDetailsVisibility) {
            View.GONE -> View.VISIBLE
            View.VISIBLE -> View.GONE
            else -> View.VISIBLE
        }
        notifyPropertyChanged(BR.notifDetailsVisibility)
    }

    @Bindable
    fun getScalable(): Boolean { return newEdict.scalable!! }
    fun setScalable(value: Boolean) {
        if (newEdict.scalable != value) {
            newEdict.scalable = value
            notifyPropertyChanged(BR.scalable)
            notifyPropertyChanged(BR.newEdict)
        }
    }

    @Bindable
    fun getCheckInStart(): String { return TimeHelper.minutesToTimeStringShort(newEdict.checkInStart) }
    fun setCheckInStart(value: Int) {
        if (newEdict.checkInStart != value) {
            newEdict.checkInStart = value
            notifyPropertyChanged(BR.checkInStart)
            notifyPropertyChanged(BR.newEdict)
        }
    }
    @Bindable
    fun getCheckInEnd(): String { return TimeHelper.minutesToTimeStringShort(newEdict.checkInEnd) }
    fun setCheckInEnd(value: Int) {
        if (newEdict.checkInEnd != value) {
            newEdict.checkInEnd = value
            notifyPropertyChanged(BR.checkInEnd)
            notifyPropertyChanged(BR.newEdict)
        }
    }

    private var doneFabClicked = false
    @Bindable
    fun getDoneFabClicked(): Boolean { return doneFabClicked }
    fun setDoneFabClicked() {
        doneFabClicked = true
        notifyPropertyChanged(BR.doneFabClicked)
        notifyPropertyChanged(BR.daysSubheaderError)
        notifyPropertyChanged(BR.actionSubheaderError)
        notifyPropertyChanged(BR.timesSubheaderError)
    }

    @Bindable
    fun getFullText(): String {
//        listener.setToolbarTitle(newEdict.toString())
        return newEdict.toString()
    }

    fun toggleDayForView(type: NewEdict.NotificationType) {
        when (newEdict.hasNotification(type)) {
            false -> addNotification(type, null)
            true -> removeNotification(type)
        }
    }

}


