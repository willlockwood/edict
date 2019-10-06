package com.willlockwood.edict.preferences

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.preference.PreferenceDialogFragmentCompat
import com.willlockwood.edict.R
import com.willlockwood.edict.utils.TimeHelper

class TimePreferenceDialog: PreferenceDialogFragmentCompat() {

    enum class DeadlineConflicts {
        MORNING_ERROR, MIDDAY_ERROR, EVENING_ERROR, NONE, OTHER_ERROR
    }

    private lateinit var picker: TimePicker

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        picker = view.findViewById(R.id.edit)

        var minutesAfterMidnight: Int? = null
        if (preference is TimePreference) {
            minutesAfterMidnight = (preference as TimePreference).getTime()
        }

        if (minutesAfterMidnight != null) { // Set the time to the TimePicker
            val hours = minutesAfterMidnight / 60
            val minutes = minutesAfterMidnight % 60
            val is24Hour = DateFormat.is24HourFormat(context)

            picker.setIs24HourView(is24Hour)
            if (Build.VERSION.SDK_INT >= 23) {
                picker.hour = hours
                picker.minute = minutes
            } else {
                picker.currentHour = hours
                picker.currentMinute = minutes
            }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {

        val deadlines = arguments!!.getIntegerArrayList("deadlines")!!
        val key = arguments!!.getString(ARG_KEY)!!

        if (positiveResult) {
            var hours: Int?
            var minutes: Int?
            if (Build.VERSION.SDK_INT >= 23) {
                hours = picker.hour;
                minutes = picker.minute;
            } else {
                hours = picker.getCurrentHour();
                minutes = picker.getCurrentMinute();
            }

            val minutesAfterMidnight = (hours * 60) + minutes

            val pref = preference
            if (pref is TimePreference) {
                val timePreference: TimePreference = pref

                when (deadlineConflicts(key, deadlines, minutesAfterMidnight)) {
                    DeadlineConflicts.MORNING_ERROR -> Toast.makeText(activity, "Morning deadline must be before the mid-day deadline.", Toast.LENGTH_LONG).show()
                    DeadlineConflicts.MIDDAY_ERROR -> Toast.makeText(activity, "Mid-day deadline must be between the morning and evening deadlines.", Toast.LENGTH_LONG).show()
                    DeadlineConflicts.EVENING_ERROR -> Toast.makeText(activity, "Evening deadline must be after the mid-day deadline.", Toast.LENGTH_LONG).show()
                    DeadlineConflicts.OTHER_ERROR -> Toast.makeText(activity, "OTHER", Toast.LENGTH_LONG).show()
                    else -> {
                        if (timePreference.callChangeListener(minutesAfterMidnight)) {
                            timePreference.setTime(minutesAfterMidnight)
                            pref.summary = TimeHelper.minutesToTimeString(minutesAfterMidnight)
//                            when (key) {
//                                "morning_deadline" -> edictVM.updateDeadlineForEdicts("morning", minutesAfterMidnight)
//                                "midday_deadline" -> edictVM.updateDeadlineForEdicts("mid-day", minutesAfterMidnight)
//                                "evening_deadline" -> edictVM.updateDeadlineForEdicts("evening", minutesAfterMidnight)
//                            }
                        }
                    }
                }
            }
        }
    }

    fun deadlineConflicts(deadlineKey: String, deadlines: ArrayList<Int>, time: Int): DeadlineConflicts {
        return when (deadlineKey) {
            "morning_deadline" -> when (time > deadlines[1]) {
                true -> DeadlineConflicts.MORNING_ERROR
                false -> DeadlineConflicts.NONE
            }
            "midday_deadline" -> when (time < deadlines[0] || time > deadlines[2]) {
                true -> DeadlineConflicts.MIDDAY_ERROR
                false -> DeadlineConflicts.NONE
            }
            "evening_deadline" -> when (time < deadlines[1]) {
                true -> DeadlineConflicts.EVENING_ERROR
                false -> DeadlineConflicts.NONE
            }
            else -> DeadlineConflicts.OTHER_ERROR
        }
    }

    companion object {
        fun newInstance(key: String, preferences: SharedPreferences): TimePreferenceDialog {
            val fragment = TimePreferenceDialog()
            val b = Bundle()
            b.putString(ARG_KEY, key)
            val morningDeadline = preferences.getInt("morning_deadline", -1)
            val middayDeadline = preferences.getInt("midday_deadline", -1)
            val eveningDeadline = preferences.getInt("evening_deadline", -1)
            b.putIntegerArrayList("deadlines", arrayListOf(morningDeadline, middayDeadline, eveningDeadline))
            fragment.arguments = b
            return fragment
        }
    }

}