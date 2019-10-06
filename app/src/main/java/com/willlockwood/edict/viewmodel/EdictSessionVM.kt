package com.willlockwood.edict.viewmodel

import android.os.CountDownTimer
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.willlockwood.edict.data.model.Edict
import java.util.*

class EdictSessionVM(
    private val edict: Edict
) : BaseObservable() {

    private var currentMinutes = 0


    init {
        val countDownTimer = object : CountDownTimer(60000 * (getSessionLength()).toLong(), 1000.toLong()) {
            override fun onFinish() {}
            override fun onTick(p0: Long) {
                val rightNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 60 + Calendar.getInstance().get(Calendar.MINUTE)
                setCurrentMinutes(rightNow)
            }
        }
        countDownTimer.start()
    }

    fun setCurrentMinutes(minutes: Int) {
        if (currentMinutes != minutes) {
            currentMinutes = minutes
            notifyPropertyChanged(BR.minutesIntoDay)
            notifyPropertyChanged(BR.minutesIntoSession)
            notifyPropertyChanged(BR.minutesIntoCheckIn)
        }
    }

    @Bindable
    fun getMinutesIntoSession(): Int { return currentMinutes - getSessionStartMinutes() }

    @Bindable
    fun getMinutesIntoDay(): Int { return currentMinutes }

    @Bindable
    fun getMinutesIntoCheckIn(): Int { return currentMinutes  - getCheckInStartMinutes() }

    private fun getSessionStartMinutes(): Int {
        return when (edict.detailType) {
            "after" -> edict.detailMinutes!!
            "between" -> edict.detailMinutes!!
            else -> 0
        }
    }

    private fun getSessionEndMinutes(): Int {
        return when (edict.detailType) {
            "before" -> edict.detailMinutes!!
            "between" -> edict.detailMinutes2!!
            else -> 1440
        }
    }

    @Bindable
    fun getSessionStart(): Float { return getSessionStartMinutes().toFloat() / 1440 }

    @Bindable
    fun getSessionEnd(): Float { return getSessionEndMinutes().toFloat() / 1440 }

    @Bindable
    fun getSessionLength(): Int { return getSessionEndMinutes() - getSessionStartMinutes() }

    @Bindable
    fun getCheckInLength(): Int { return getCheckInEndMinutes() - getCheckInStartMinutes() }

    @Bindable
    fun getCheckInStart(): Float { return getCheckInStartMinutes().toFloat() / 1440 }

    @Bindable
    fun getCheckInEnd(): Float { return getCheckInEndMinutes().toFloat() / 1440 }

    private fun getCheckInStartMinutes(): Int { return getSessionStartMinutes() }
    private fun getCheckInEndMinutes(): Int { return edict.deadlineMinutes }

}