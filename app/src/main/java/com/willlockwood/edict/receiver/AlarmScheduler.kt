package com.willlockwood.edict.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.willlockwood.edict.utils.TimeHelper
import java.util.*


object AlarmScheduler {

    val NONE = 0
    val DAILY_REMINDER_REQUEST_CODE = 100
    val DAILY_REFRESH_REQUEST_CODE = 101
    val TAG = "NotifScheduler"

    enum class NotifType {
        REVIEW_EDICTS, CHECK_IN
    }

    enum class AlarmType {
        REVIEW_EDICTS, CHECK_IN, REFRESH_SESSIONS
    }

    fun scheduleAlarm(context: Context, cls: Class<*>, minutes: Int, alarmType: AlarmType) {
        Log.d(TAG, "scheduleAlarm():")
        var requestCode = NONE
        var intentCode = "none"
        var shouldRepeat = false
        var repeatInterval = AlarmManager.INTERVAL_DAY
        when (alarmType) {
            AlarmScheduler.AlarmType.REFRESH_SESSIONS -> {
                requestCode = DAILY_REFRESH_REQUEST_CODE
                intentCode = "refresh_sessions"
                shouldRepeat = true
                repeatInterval = AlarmManager.INTERVAL_DAY
            }
            else -> DAILY_REMINDER_REQUEST_CODE
        }

        val rightNow = Calendar.getInstance()
        val setCalendar = Calendar.getInstance()

//        setCalendar.set(Calendar.HOUR_OF_DAY, hour)
//        setCalendar.set(Calendar.MINUTE, min)
        setCalendar.set(Calendar.HOUR_OF_DAY, TimeHelper.getHoursIntFromMinutes(minutes))
        setCalendar.set(Calendar.MINUTE, TimeHelper.getMinutesIntFromMinutes(minutes))
        setCalendar.set(Calendar.SECOND, 0)

        cancelReminder(context, cls, requestCode) // cancel existing reminders

        if (setCalendar.before(rightNow)) setCalendar.add(Calendar.DATE,1) // If the alarm is for before right now, make it for tomorrow

        val receiver = ComponentName(context, cls) // Enable a receiver
        val packageManager = context.packageManager
        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, cls)
        val extras = Bundle()
        intent.action = intentCode
        intent.putExtra("action", intentCode)
        extras.putString("action1", intentCode)
        intent.putExtras(extras)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (shouldRepeat) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.timeInMillis, repeatInterval, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, setCalendar.timeInMillis, pendingIntent)
        }

    }


    private fun cancelReminder(context: Context, cls: Class<*>, requestCode: Int) {
        // Disable a receiver
        val receiver = ComponentName(context, cls)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)

        val intent = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }


}

