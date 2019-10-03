package com.willlockwood.edict.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import androidx.annotation.RequiresApi
import com.willlockwood.edict.data.database.EdictDatabase
import com.willlockwood.edict.data.model.EdictSession
import com.willlockwood.edict.notification.NotifHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class EdictJobService : JobService() {

    enum class JobID {
        NEW_ACTIVE_SESSIONS_NOTIFICATION, SEND_EXTRA_NOTIFICATION
    }

    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        return when (jobParameters?.jobId) {
            JobID.NEW_ACTIVE_SESSIONS_NOTIFICATION.ordinal -> notifyNewActiveSessions()
            JobID.SEND_EXTRA_NOTIFICATION.ordinal -> sendExtraNotifications(jobParameters)
            else -> false
        }
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    private fun sendExtraNotifications(jobParameters: JobParameters?): Boolean {
        val extras = jobParameters!!.extras
        NotifHelper.createExtraNotification(applicationContext, extras)
        return false
    }

    private fun notifyNewActiveSessions(): Boolean {
        val database = EdictDatabase.getDatabase(applicationContext, GlobalScope)

        // TODO: check if this observeForever is a problem
        database.edictDao().getAllEdicts().observeForever {
            if (it != null) {
                var sessions = emptyList<EdictSession>()
                for (edict in it) {
                    if (edict.isActiveToday()) {
                        sessions = sessions.plus(edict.createEdictSession())
                    }
                }
                if (sessions.isNotEmpty()) {
                    GlobalScope.launch {
                        database.edictSessionDao().insertEdictSessions(sessions)
                    }
                    NotifHelper.createNewSessionsNotification(applicationContext)
                }
            }
        }
        return true
    }


}