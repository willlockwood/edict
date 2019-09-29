package com.willlockwood.edict.app

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import com.willlockwood.edict.R
import com.willlockwood.edict.notification.NotifHelper

class EdictApp: Application() {

    companion object {
        lateinit var instance: EdictApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        AndroidThreeTen.init(this)

        NotifHelper.createNotificationChannel(this,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")
    }
}