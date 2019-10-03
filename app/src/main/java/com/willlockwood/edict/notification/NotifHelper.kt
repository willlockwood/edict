package com.willlockwood.edict.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.willlockwood.edict.R
import com.willlockwood.edict.activity.MainActivity
import com.willlockwood.edict.utils.TimeHelper

object NotifHelper {

    private const val TAG = "NotifHelper"

    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun createExtraNotification(context: Context, intentExtras: PersistableBundle) {
        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        val edicts = intentExtras.getString("edicts")?.split(";")!!
        val deadlines = intentExtras.getString("deadlines")?.split(";")!!
        val notifyTypes = intentExtras.getString("notifyTypes")?.split(";")!!

        var contentTitle = ""
        var contentText = ""
        for ((i, type) in notifyTypes.withIndex()) {
            when (type) {
                "start" -> {
                    contentTitle = "A new edict is starting!"
                    contentText = edicts[i]
                }
                "end" -> {
                    contentTitle = "\"${edicts[i]}\" is ending!"
                    contentText = "Check in by ${TimeHelper.minutesToTimeString(deadlines[i].toInt())}"
                }
                "at" -> {
                    contentTitle = "Reminder: ${edicts[i]}"
                    contentText = "Check in by ${TimeHelper.minutesToTimeString(deadlines[i].toInt())}"
                }
            }
        }

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(contentTitle)
            setContentText(contentText)
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)

            val notificationIntent = Intent(context, MainActivity::class.java) // This is the intent attached to the notification
            val extras = Bundle()
            // TODO: figure out a better system than putting string extras to identify notification types
            extras.putString("action", "extra_notification")
            notificationIntent.putExtras(extras)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
            setContentIntent(pendingIntent)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        // TODO: check what 1001 is doing here
        notificationManager.notify(1001, notificationBuilder.build())
    }

    fun createNewSessionsNotification(context: Context) {
        Log.i(TAG, "createNewSessionsNotification")

        val channelId = "${context.packageName}-${context.getString(R.string.app_name)}"

        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(context.getString(R.string.notif_new_sessions_title))
            setContentText(context.getString(R.string.notif_new_sessions_message))
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)

            // This is the intent attached to the notification
            val notificationIntent = Intent(context, MainActivity::class.java)
            val extras = Bundle()
            // TODO: figure out a better system than putting string extras to identify notification types
            extras.putString("action", "review")
            notificationIntent.putExtras(extras)
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
            setContentIntent(pendingIntent)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        // TODO: check what 1001 is doing here
        notificationManager.notify(1001, notificationBuilder.build())
    }
}