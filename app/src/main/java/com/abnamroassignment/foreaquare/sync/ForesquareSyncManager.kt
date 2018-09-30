package com.abnamroassignment.foreaquare.sync

import android.content.Context
import android.util.Log
import com.abnamroassignment.foreaquare.DataSource
import com.abnamroassignment.foreaquare.StorageDataSource
import com.abnamroassignment.foreaquare.datasource.local.DatabaseDataSource
import com.abnamroassignment.foreaquare.datasource.remote.RetrofitDataSource


class ForesquareSyncManager(private val remoteDataSource: DataSource,
                            private val localDataSource: StorageDataSource) {

    private val tag = ForesquareSyncManager::class.java.simpleName

    constructor(context:Context): this(RetrofitDataSource(context),
            DatabaseDataSource(context))

    fun syncLocalVenuesWithServer() {
        localDataSource.getAllVenueIds().forEach {
            val venueDetailsResult = remoteDataSource.fetchVenueDetails(it)
            venueDetailsResult.venueDetails?.apply {
                localDataSource.saveVenueDetails(this)
                Log.d(tag, "Sync successful for venue $id")
            }?:Log.w(tag, "Sync failed for venue ${venueDetailsResult.status}")
        }
    }
}