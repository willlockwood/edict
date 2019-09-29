package com.willlockwood.edict.receiver

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.willlockwood.edict.service.EdictJobService

class AlarmReceiver: BroadcastReceiver() {

    val TAG = "AlarmReceiver"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "onReceive: ")

        val action_string = intent?.getStringExtra("action")

        when (action_string) {
            "refresh_sessions" -> {
                val jobScheduler: JobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                val componentName = ComponentName(context, EdictJobService::class.java)
                val jobInfo = JobInfo.Builder(EdictJobService.JobID.NEW_ACTIVE_SESSIONS_NOTIFICATION.ordinal, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NOT_ROAMING)
                    .build()
                jobScheduler.schedule(jobInfo)
            }
        }
    }
}