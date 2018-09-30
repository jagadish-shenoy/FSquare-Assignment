package com.abnamroassignment.foreaquare.sync

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log

class SyncJobScheduler(private val context: Context) {

    private val tag = SyncJobScheduler::class.java.simpleName

    companion object {
        const val VENUE_SYNC_JOB_ID = 100
    }

    private val jobScheduler =  context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

    fun scheduleSyncJob() {
        if(jobScheduler.allPendingJobs.isEmpty()) {
            Log.i(tag, "Scheduled background sync job")
            val jobInfo = JobInfo.Builder(VENUE_SYNC_JOB_ID, ComponentName(context, VenueSyncService::class.java))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).build()
            jobScheduler.schedule(jobInfo)
        }
    }
}