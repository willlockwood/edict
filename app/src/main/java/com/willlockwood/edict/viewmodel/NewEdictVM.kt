package com.willlockwood.edict.viewmodel

import android.content.SharedPreferences
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.android.material.chip.Chip
import com.willlockwood.edict.R
import com.willlockwood.edict.data.model.Edict
import com.willlockwood.edict.utils.EdictHelper
import com.willlockwood.edict.utils.TimeHelper

class NewEdictVM(
    private val sharedPreferences: SharedPreferences
) : BaseObservable() {

    private var edict: Edict = Edict()
    private var edictText = edict.toString()

    var baseTypes = arrayOf("Never", "Only", "Routine:")
    private var baseTypeIdx = 0

    private var detailTypes: Array<String> = arrayOf("between", "ever", "after", "before", "before/after", "on", "every")
    private var detailTypeIdx = 0

    var everyTypes = arrayOf("time", "morning", "day", "night", "week", "#")
    private var everyTypeIdx = 0

    var everyNumbers = arrayOf("2", "3", "4", "5", "6", "7", "8", "9", "10")
    private var everyNumberIdx = 0

    var everyNumberTimes = arrayOf("days", "weeks")
    private var everyNumberTimeIdx = 0

    var numberTimes = arrayOf("2", "3", "4", "5", "6", "7", "8", "9", "10")
    private var numberTimeIdx = 0

    var numberTimesPerTimes = arrayOf("day", "week")
    private var numberTimesPerTimeIdx = 0

    var deadlines = arrayOf("evening", "mid-day", "morning", "at")
    private var deadlineIdx = 0


    private var onDays = hashMapOf(
        Pair("Sunday", false),
        Pair("Monday", false),
        Pair("Tuesday", false),
        Pair("Wednesday", false),
        Pair("Thursday", false),
        Pair("Friday", false),
        Pair("Saturday", false)
    )

    init {
        setTypeIdx(1)
        setTypeIdx(0)
        setActivity("do something")
        setDeadlineIdx(1)
        setDeadlineIdx(0)
    }

    @Bindable
    fun getEdictStatus(): EdictHelper.ErrorStatus {
        return edict.getErrorStatus()
    }

    @Bindable
    fun getActivity(): String { return edict.activity }
    fun setActivity(value: String) {
        if (edict.activity != value) {
            edict.activity = value
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.activity)
        }
    }

    ////////////////// BASE TYPES //////////////////////////////////////////////
    @Bindable
    fun getTypeIdx(): Int { return baseTypeIdx }
    fun setTypeIdx(idx: Int) {
        if (baseTypeIdx != idx) {
            baseTypeIdx = idx
            edict.type = baseTypes[baseTypeIdx]

            notifyPropertyChanged(BR.typeIdx)

            setDetailTypes(edict.type)

            setEdictText(edict.toString())
        }
    }

    ////////////////// DETAIL TYPES ///////////////////////
    @Bindable
    fun getDetailTypes(): Array<String> { return detailTypes }
    private fun setDetailTypes(baseType: String) {
        detailTypes = when(baseType) {
            "Routine:" -> arrayOf("every", "on", "#")
            "Never" -> arrayOf("after", "at", "before", "between", "ever", "on", "when", "while")
            "Only" -> arrayOf("after", "at", "before", "between", "on", "when", "while")
            else -> arrayOf("after", "before", "between", "on")
        }

        setDetailTypeIdx(1)
        setDetailTypeIdx(0)

        notifyPropertyChanged(BR.detailTypes)
    }

    @Bindable
    fun getDetailTypeIdx(): Int { return detailTypeIdx }
    fun setDetailTypeIdx(idx: Int) {
        if (detailTypeIdx != idx) {

            when (detailTypes[detailTypeIdx]) {
                "at" ->     setAtText(null)
                "after" -> setDetailMinutesText(null)
                "before" -> setDetailMinutesText(null)
                "between" -> {  setDetailMinutesText(null)
                                setDetailMinutes2Text(null) }
                "while" ->  setWhileText(null)
                "when" ->   setWhenText(null)
                "every" ->  setEveryTypeIdx(null)
                "#" -> {    setNumberTimeIdx(null)
                            setNumberTimesPerTimeIdx(null) }
                "on" -> {   setOnDaysOfTheWeek(null)
                            setAllChipsAreUnchecked(true)
                            setOnText(null) }
            }

            detailTypeIdx = idx
            val selection = detailTypes[detailTypeIdx]

            setDetailType(selection)

            when (edict.detailType) {
                "at" ->     setAtText("some location")
                "after" ->  setDetailMinutesText("12:30 PM")
                "before" ->  setDetailMinutesText("12:30 PM")
                "between" -> {  setDetailMinutesText("8:00 AM")
                                setDetailMinutes2Text("4:00 PM") }
                "while" ->  setWhileText("doing something else")
                "when" ->   setWhenText("something else happens")
                "every" ->  setEveryTypeIdx(0)
                "#" -> {    setNumberTimeIdx(0)
                    setNumberTimesPerTimeIdx(0) }
                "on" -> {   setOnDaysOfTheWeek(null)
                    setAllChipsAreUnchecked(true)
                    setOnText("workdays") }
            }
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.detailType)
            notifyPropertyChanged(BR.detailTypeIdx)
        }
    }

    @Bindable // "[before, between, on...]" 3:00 am
    fun getDetailType(): String { return edict.detailType }
    private fun setDetailType(value: String) {
        if (edict.detailType != value) {
            edict.detailType = value
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.detailType)
        }
    }

    ////////////////// EVERY TYPES //////////////////////////////////////////////
    @Bindable
    fun getEveryTypeIdx(): Int? { return everyTypeIdx }
    fun setEveryTypeIdx(idx: Int?) {
        if (idx == null) {
            edict.everyType = null
            setEveryNumberIdx(null)
            setEveryNumberTimeIdx(null)
            everyTypeIdx = 0
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.everyTypeIdx)
        } else if (everyTypeIdx != idx || edict.everyType == null) {

            when (everyTypes[everyTypeIdx]) {
                "#" -> {
                    setEveryNumberIdx(null)
                    setEveryNumberTimeIdx(null)
                }
                "time" -> setEveryTimeText(null)
            }

            everyTypeIdx = idx
            val everySelection = everyTypes[everyTypeIdx]
            setEveryType(everySelection)

            when (everySelection) {
                "#" -> {
                    setEveryNumberIdx(0)
                    setEveryNumberTimeIdx(0)
                }
                "time" -> setEveryTimeText("something happens")
            }

            setEdictText(edict.toString())
            notifyPropertyChanged(BR.everyType)
            notifyPropertyChanged(BR.everyTypeIdx)
        }
    }

    @Bindable
    fun getEveryType(): String? { return edict.everyType }
    private fun setEveryType(value: String?) {
        if (edict.everyType != value || edict.everyType == null) {
            edict.everyType = value
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.everyType)
        }
    }

    @Bindable // "every ___ days"
    fun getEveryNumberIdx(): Int? { return everyNumberIdx }
    fun setEveryNumberIdx(idx: Int?) {
        if (idx == null) {
            edict.everyNumber = idx
            everyNumberIdx = 0
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.everyNumberIdx)
        } else if (everyNumberIdx != idx || edict.everyNumber == null) {
            everyNumberIdx = idx
            edict.everyNumber = everyNumbers[everyNumberIdx].toInt()
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.everyNumberIdx)
        }
    }

    @Bindable // "every 3 [days/weeks]"
    fun getEveryNumberTimeIdx(): Int? { return everyNumberTimeIdx }
    fun setEveryNumberTimeIdx(value: Int?) {
        if (value == null) {
            edict.everyNumberTime = value
            everyNumberIdx = 0
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.everyNumberTimeIdx)
        } else if (everyNumberTimeIdx != value || edict.everyNumberTime == null) {
            everyNumberTimeIdx = value
            edict.everyNumberTime = everyNumberTimes[everyNumberTimeIdx]
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.everyNumberTimeIdx)
        }
    }

    @Bindable // "every time ____"
    fun getEveryTimeText(): String? { return edict.everyTimeText }
    fun setEveryTimeText(value: String?) {
        if (value == "") {
            setEveryTimeText(null)
        } else if (edict.everyTimeText != value) {
            edict.everyTimeText = value
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.everyTimeText)
        }
    }

    ////////////////// NUMBER TYPES //////////////////////////////////////////////
    @Bindable
    fun getNumberTimesPerTimeIdx(): Int? { return numberTimesPerTimeIdx }
    fun setNumberTimesPerTimeIdx(value: Int?) {
        if (value == null) {
            edict.numberTimesPerTime = value
            numberTimesPerTimeIdx = 0
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.numberTimesPerTimeIdx)
        } else if (numberTimesPerTimeIdx != value) {
            numberTimesPerTimeIdx = value
            edict.numberTimesPerTime = numberTimesPerTimes[numberTimesPerTimeIdx]
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.numberTimesPerTimeIdx)
        }
    }

    @Bindable
    fun getNumberTimeIdx(): Int? { return numberTimeIdx }
    fun setNumberTimeIdx(value: Int?) {
        if (value == null) {
            edict.numberTime = value
            numberTimeIdx = 0
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.numberTimeIdx)
        } else if (numberTimeIdx != value) {
            numberTimeIdx = value
            edict.numberTime = numberTimes[numberTimeIdx].toInt()
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.numberTimeIdx)
        }
    }

    ////////////////// OTHER //////////////////////////////////////////////
    @Bindable // "at ___"
    fun getAtText(): String? { return edict.atText }
    fun setAtText(value: String?) {
        if (edict.atText != value) {
            edict.atText = value
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.atText)
        }
    }

    @Bindable
    fun getDetailMinutesText(): String? { return TimeHelper.minutesToTimeString(edict.detailMinutes) }
    fun setDetailMinutesText(value: String?) {
        val minutes: Int? = TimeHelper.getMinutesFromTimeString(value)
        if (edict.detailMinutes != minutes || minutes == null || value == null) {
            edict.detailMinutes = minutes
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.detailMinutesText)
        }
    }

    @Bindable
    fun getDetailMinutes2Text(): String? { return TimeHelper.minutesToTimeString(edict.detailMinutes2) }
    fun setDetailMinutes2Text(value: String?) {
        val minutes = TimeHelper.getMinutesFromTimeString(value)
        if (edict.detailMinutes2 != minutes || minutes == null || value == null) {
            edict.detailMinutes2 = minutes
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.detailMinutes2Text)
        }
    }



    @Bindable // "on ___"
    fun getOnText(): String? { return edict.onText }
    fun setOnText(value: String?) {
        if (edict.onText != value || edict.onText == null) {
            if (value != null && value != "") {
                edict.onDaysOfTheWeek = null
            }
            edict.onText = value
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.onText)
        }
    }

    @Bindable // "while ___"
    fun getWhileText(): String? { return edict.whileText }
    fun setWhileText(value: String?) {
        if (edict.whileText != value) {
            edict.whileText = value
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.whileText)
        }
    }

    @Bindable // "when ___"
    fun getWhenText(): String? { return edict.whenText }
    fun setWhenText(value: String?) {
        if (edict.whenText != value) {
            edict.whenText = value
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.whenText)
        }
    }

    @Bindable
    fun getEdictText(): String { return edictText }
    private fun setEdictText(value: String) {
        if (edictText != value) {
            edictText = value
            notifyPropertyChanged(BR.edictText)
        }
        notifyPropertyChanged(BR.edictStatus)
    }

    private fun setOnDaysOfTheWeek(value: String?) {
        if (edict.onDaysOfTheWeek != value) {
            edict.onDaysOfTheWeek = value
            if (value == null) {
                for (key in onDays.keys) {
                    onDays[key] = false
                }
            }
            setEdictText(edict.toString())
        }
    }

    fun addDay(view: View) {
        val day = when (view.id) {
            R.id.sun_chip -> "Sunday"
            R.id.mon_chip -> "Monday"
            R.id.tue_chip -> "Tuesday"
            R.id.wed_chip -> "Wednesday"
            R.id.thu_chip -> "Thursday"
            R.id.fri_chip -> "Friday"
            R.id.sat_chip -> "Saturday"
            else -> "this shouldn't be the case"
        }
        onDays[day] = (view as Chip).isChecked
        var daysList = emptyList<String>()
        for (d in listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")) {
            if (onDays[d]!!) {
                daysList = daysList + d
            }
        }
        if (daysList.isEmpty()) {
            edict.onDaysOfTheWeek = null
            setAllChipsAreUnchecked(true)
            setOnText("workdays")
        } else {
            setOnText(null)
            setAllChipsAreUnchecked(false)
            edict.onDaysOfTheWeek = daysList.joinToString(", ")
        }
        setEdictText(edict.toString())
    }

    private var allChipsAreUnchecked = true
    @Bindable
    fun getAllChipsAreUnchecked(): Boolean { return allChipsAreUnchecked }
    private fun setAllChipsAreUnchecked(value: Boolean) {
        if (allChipsAreUnchecked != value) {
            allChipsAreUnchecked = value
            notifyPropertyChanged(BR.allChipsAreUnchecked)
        }
    }


    private var notifyStartTime: String? = null
    @Bindable
    fun getNotifyStartTime(): String { return "($notifyStartTime)" }
    fun setNotifyStartTime(notifyStartTime: String?) {
        if (this.notifyStartTime != notifyStartTime) {
            this.notifyStartTime = notifyStartTime
            notifyPropertyChanged(BR.notifyStartTime)
        }
    }

    private var notifyEndTime: String? = null
    @Bindable
    fun getNotifyEndTime(): String { return "($notifyEndTime)" }
    fun setNotifyEndTime(notifyEndTime: String?) {
        if (this.notifyEndTime != notifyEndTime) {
            this.notifyEndTime = notifyEndTime
            notifyPropertyChanged(BR.notifyEndTime)
        }
    }

    private var notifyAtTime: String? = null
    @Bindable
    fun getNotifyAtTime(): String? { return this.notifyAtTime }
    fun setNotifyAtTime(notifyAtTime: String?) {
        if (this.notifyAtTime != notifyAtTime) {
            this.notifyAtTime = notifyAtTime
            if (notifyAtToggled) {
                edict.notifyAtMinutes = TimeHelper.getMinutesFromTimeString(notifyAtTime)
            }
            notifyPropertyChanged(BR.notifyAtTime)
        }
    }

    private var notifyStartToggled = false
    @Bindable
    fun getNotifyStartToggled(): Boolean { return notifyStartToggled }
    fun setNotifyStartToggled(notifyStartToggled: Boolean) {
        if (this.notifyStartToggled != notifyStartToggled) {
            this.notifyStartToggled = notifyStartToggled
            edict.notifyStartMinutes = when (notifyStartToggled) {
                true -> TimeHelper.getMinutesFromTimeString(this.notifyStartTime)
                false -> null
            }
            notifyPropertyChanged(BR.notifyStartToggled)
        }
    }

    private var notifyEndToggled: Boolean = false
    @Bindable
    fun getNotifyEndToggled(): Boolean { return notifyEndToggled }
    fun setNotifyEndToggled(notifyEndToggled: Boolean) {
        if (this.notifyEndToggled != notifyEndToggled) {
            this.notifyEndToggled = notifyEndToggled
            edict.notifyEndMinutes = when (notifyEndToggled) {
                true -> TimeHelper.getMinutesFromTimeString(this.notifyEndTime)
                false -> null
            }
            notifyPropertyChanged(BR.notifyEndToggled)
        }
    }

    private var notifyAtToggled: Boolean = false
    @Bindable
    fun getNotifyAtToggled(): Boolean { return notifyAtToggled }
    fun setNotifyAtToggled(notifyAtToggled: Boolean) {
        if (this.notifyAtToggled != notifyAtToggled) {
            this.notifyAtToggled = notifyAtToggled
            edict.notifyAtMinutes = when (notifyAtToggled) {
                true -> TimeHelper.getMinutesFromTimeString(this.notifyAtTime)
                else -> null
            }
            notifyPropertyChanged(BR.notifyAtToggled)
        }
    }

    private var finalizeButtonPressed: Boolean = false
    @Bindable
    fun getFinalizeButtonPressed(): Boolean { return finalizeButtonPressed }
    fun setFinalizeButtonPressed(value: Boolean) {
        if (finalizeButtonPressed != value) {
            finalizeButtonPressed = value
            notifyPropertyChanged(BR.finalizeButtonPressed)
        }
    }

    private var currentlyFinalizing: Boolean = false
    @Bindable
    fun getCurrentlyFinalizing(): Boolean { return currentlyFinalizing }
    fun setCurrentlyFinalizing(value: Boolean) {
        if (currentlyFinalizing != value) {
            currentlyFinalizing = value

            when (edict.detailType) {
                "before" -> {
                    setNotifyEndTime(TimeHelper.minutesToTimeString(edict.detailMinutes))
                    setNotifyEndToggled(true)
                    setNotifyEndToggled(false)
                    setNotifyAtTime(TimeHelper.minutesToTimeString(edict.detailMinutes))
                    setNotifyAtToggled(true)
                    setNotifyAtToggled(false)
                }
                "after" -> {
                    setNotifyStartTime(TimeHelper.minutesToTimeString(edict.detailMinutes))
                    setNotifyStartToggled(true)
                    setNotifyStartToggled(false)
                    setNotifyAtTime(TimeHelper.minutesToTimeString(edict.detailMinutes))
                    setNotifyAtToggled(true)
                    setNotifyAtToggled(false)
                }
                "between" -> {
                    setNotifyStartTime(TimeHelper.minutesToTimeString(edict.detailMinutes))
                    setNotifyStartToggled(true)
                    setNotifyStartToggled(false)
                    setNotifyEndTime(TimeHelper.minutesToTimeString(edict.detailMinutes2))
                    setNotifyEndToggled(true)
                    setNotifyEndToggled(false)
                    setNotifyAtTime(TimeHelper.minutesToTimeString(edict.detailMinutes2))
                    setNotifyAtToggled(true)
                    setNotifyAtToggled(false)
                }
                else -> {
                    setNotifyAtTime(TimeHelper.minutesToTimeString(720))
                    setNotifyAtToggled(true)
                    setNotifyAtToggled(false)
                }

            }
            notifyPropertyChanged(BR.currentlyFinalizing)
        }
    }

    private var uploadButtonPressed: Boolean = false
    @Bindable
    fun getUploadButtonPressed(): Boolean { return uploadButtonPressed }
    fun setUploadButtonPressed(value: Boolean) {
        if (uploadButtonPressed != value) {
            uploadButtonPressed = value
            notifyPropertyChanged(BR.uploadButtonPressed)
        }
    }

    @Bindable
    fun getDeadlineText(): String? { return TimeHelper.minutesToTimeString(edict.deadlineMinutes) }
    fun setDeadlineText(value: String?) {
        val minutes: Int? = TimeHelper.getMinutesFromTimeString(value)
        if (edict.deadlineMinutes != minutes || minutes == null || value == null) {
            edict.deadlineMinutes = minutes
            setEdictText(edict.toString())
            notifyPropertyChanged(BR.deadlineText)
        }
    }

    @Bindable
    fun getDeadlineIdx(): Int { return deadlineIdx }
    fun setDeadlineIdx(idx: Int) {
        if (deadlineIdx != idx) {
            deadlineIdx = idx

            val deadlineType = deadlines[deadlineIdx]
            setDeadlineType(deadlineType)

            notifyPropertyChanged(BR.deadlineIdx)
        }
    }

    @Bindable // ["morning", "midday", "evening", "at"]
    fun getDeadlineType(): String? { return edict.deadlineType }
    private fun setDeadlineType(value: String) {
        if (edict.deadlineType != value) {
            edict.deadlineType = value
            notifyPropertyChanged(BR.deadlineType)

            val minutes = when (edict.deadlineType) {
                "morning" -> sharedPreferences.getInt("morning_deadline", 5)
                "mid-day" -> sharedPreferences.getInt("midday_deadline", 5)
                "evening" -> sharedPreferences.getInt("evening_deadline", 5)
                else -> TimeHelper.getMinutesFromTimeString(getDeadlineText())
            }
            setDeadlineText(TimeHelper.minutesToTimeString(minutes))
        }
    }

    private fun resetShouldNotify() {
        val shouldNotifyAt = getNotifyAtToggled()
        val shouldNotifyAtEnd = getNotifyEndToggled()
        val shouldNotifyAtStart = getNotifyStartToggled()
        if (!shouldNotifyAt) { edict.notifyAtMinutes = null }
        if (!shouldNotifyAtStart) { edict.notifyStartMinutes = null }
        if (!shouldNotifyAtEnd) { edict.notifyEndMinutes = null }
        val blah = edict
    }

    fun getEdict(): Edict {
//        resetShouldNotify()
        return edict
    }

}