package com.willlockwood.edict.notification


object NotifScheduler {

    val DAILY_REMINDER_REQUEST_CODE = 100
    val TAG = "NotifScheduler"

    enum class NotifType {
        REVIEW_EDICTS, CHECK_IN
    }

//    fun setReminder(context: Context, cls: Class<*>, hour: Int, min: Int, type: NotifType) {
//        Log.i(TAG, "setReminder")
//        val rightNow = Calendar.getInstance()
//
//        val setCalendar = Calendar.getInstance()
//        setCalendar.set(Calendar.HOUR_OF_DAY, hour)
//        setCalendar.set(Calendar.MINUTE, min)
//        setCalendar.set(Calendar.SECOND, 0)
//
//        // Cancel already scheduled reminders
//        cancelReminder(context,cls)
//
//        if (setCalendar.before(rightNow)) setCalendar.add(Calendar.DATE,1)
//
//        // Enable a receiver
//        val receiver = ComponentName(context, cls)
//        val packageManager = context.packageManager
//
//        packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
//
//        val intent = Intent(context, cls)
//        val extras = Bundle()
//        extras.putString("destination", "review")
//        intent.putExtras(extras)
//        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
//        alarmManager.setInexactRepeating(
//            AlarmManager.RTC_WAKEUP, // Alarm type
//            setCalendar.timeInMillis, // When to trigger
//            AlarmManager.INTERVAL_FIFTEEN_MINUTES, // Repeat interval
//            pendingIntent // intent operation
//        )
//    }
//
//    private fun cancelReminder(context: Context, cls: Class<*>) {
//
//        // Disable a receiver
//        val receiver = ComponentName(context, cls)
//        val pm = context.packageManager
//
//        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
//
//        val intent = Intent(context, cls)
//        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
//        alarmManager.cancel(pendingIntent)
//        pendingIntent.cancel()
//    }
}