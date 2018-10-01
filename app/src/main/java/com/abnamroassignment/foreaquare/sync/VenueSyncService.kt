package com.abnamroassignment.foreaquare.sync

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VenueSyncService : JobService() {

    private val tag = VenueSyncService::class.java.simpleName

    private lateinit var syncExecutor: ExecutorService

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(tag, "Background venue sync started.")
        syncExecutor = Executors.newSingleThreadExecutor()

        syncExecutor.submit {
            ForesquareSyncManager(applicationContext).syncLocalVenuesWithServer()
            //Keep rescheduling - every day job!
            jobFinished(params, true)
            Log.i(tag, "Background venue sync finished.")
        }
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.e(tag, "Background venue sync forced to stop")
        syncExecutor.shutdownNow()
        return true
    }
}