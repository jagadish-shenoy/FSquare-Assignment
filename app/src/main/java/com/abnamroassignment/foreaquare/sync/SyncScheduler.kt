package com.abnamroassignment.foreaquare.sync

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import java.util.concurrent.TimeUnit

class SyncJobScheduler(private val context: Context) {

    private val tag = SyncJobScheduler::class.java.simpleName

    companion object {
        const val VENUE_SYNC_JOB_ID = 100
    }

    private val jobScheduler =  context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

    /**
     * Schedules a job to sync the cached venue details once per day when network is available.
     */
    fun scheduleSyncJob() {
        if(jobScheduler.allPendingJobs.isEmpty()) {
            Log.i(tag, "Scheduled background sync job")
            val jobInfo = JobInfo.Builder(VENUE_SYNC_JOB_ID, ComponentName(context, VenueSyncService::class.java))
                    .setPeriodic(TimeUnit.DAYS.toMillis(1))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).build()
            jobScheduler.schedule(jobInfo)
        }
    }
}