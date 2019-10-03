package com.willlockwood.edict.preferences

import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker
import androidx.preference.PreferenceDialogFragmentCompat
import com.willlockwood.edict.R
import com.willlockwood.edict.utils.TimeHelper

class TimePreferenceDialog: PreferenceDialogFragmentCompat() {

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
                if (timePreference.callChangeListener(minutesAfterMidnight)) {
                    timePreference.setTime(minutesAfterMidnight)
                    pref.summary = TimeHelper.minutesToTimeString(minutesAfterMidnight)
                }
            }
        }
    }

    companion object {
        fun newInstance(key: String): TimePreferenceDialog {
            val fragment = TimePreferenceDialog()
            val b = Bundle()
            b.putString(ARG_KEY, key)
            fragment.arguments = b
            return fragment
        }
    }

}