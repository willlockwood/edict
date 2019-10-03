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
import androidx.core.os.persistableBundleOf
import com.willlockwood.edict.service.EdictJobService

class AlarmReceiver: BroadcastReceiver() {

    val TAG = "AlarmReceiver"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "onReceive: ")

        val action_string = intent!!.getStringExtra("action")
        val ids = intent.getStringExtra("ids")
        val time = intent.getIntExtra("time", -1)
        val edicts = intent.getStringExtra("edicts")
        val deadlines = intent.getStringExtra("deadlines")
        val notifyTypes = intent.getStringExtra("notifyTypes")

        when (action_string) {
            "refresh_sessions" -> {
                val jobScheduler: JobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                val componentName = ComponentName(context, EdictJobService::class.java)
                val jobInfo = JobInfo.Builder(EdictJobService.JobID.NEW_ACTIVE_SESSIONS_NOTIFICATION.ordinal, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NOT_ROAMING)
                    .build()
                jobScheduler.schedule(jobInfo)
            }
            "extra_notifications" -> {
                val jobScheduler: JobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                val componentName = ComponentName(context, EdictJobService::class.java)
                val persistableBundle= persistableBundleOf("ids" to ids, "time" to time, "edicts" to edicts, "deadlines" to deadlines, "notifyTypes" to notifyTypes)
                val jobInfo = JobInfo.Builder(EdictJobService.JobID.SEND_EXTRA_NOTIFICATION.ordinal, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NOT_ROAMING)
                    .setExtras(persistableBundle)
                    .build()
                jobScheduler.schedule(jobInfo)
            }
            else -> Log.i(TAG, "neither kind")


        }
    }
}