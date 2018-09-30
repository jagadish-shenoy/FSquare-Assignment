package com.abnamroassignment.foreaquare

import android.content.Context
import com.abnamroassignment.foreaquare.FourSquareManager.ForeSquareManagerCallback
import com.abnamroassignment.foreaquare.datasource.local.DatabaseDataSource
import com.abnamroassignment.foreaquare.datasource.remote.RetrofitDataSource
import com.abnamroassignment.foreaquare.sync.SyncJobScheduler
import com.abnamroassignment.networking.ConnectivityChecker
import com.abnamroassignment.networking.ConnectivityCheckerImpl
import java.util.concurrent.Executors

/**
 * Entry point for all the ForeSquare API requests from the UI.
 * Details whether to fetch the Venue data from network or local storage is made by this
 * class based on connectivity status from [ConnectivityChecker]
 *
 * Requests for background data sync, iff any request was served from the local storage data source.
 *
 * Results of the API calls are notified asynchronously via [ForeSquareManagerCallback]
 *
 */
class FourSquareManager private constructor(private val callback: ForeSquareManagerCallback,
                                            private val connectivityChecker: ConnectivityChecker,
                                            private val syncJobScheduler: SyncJobScheduler,
                                            private val localDataSource: StorageDataSource,
                                            private val remoteDataSource: DataSource) {

    private val executor = Executors.newSingleThreadExecutor()

    constructor(context: Context, callback: ForeSquareManagerCallback) : this(callback,
            ConnectivityCheckerImpl(context),
            SyncJobScheduler(context),
            DatabaseDataSource(context),
            RetrofitDataSource(context))

    interface ForeSquareManagerCallback {

        fun onVenueSearchResponse(venueSearchResult: VenueSearchResult)

        fun onVenueDetailsResponse(venueDetailsResult: VenueDetailsResult)
    }

    fun searchVenues(location: String) {
        executor.submit { searchVenues(getDataSource(), location) }
    }

    private fun searchVenues(dataSource: DataSource, location: String) {
        val venueSearchResult = dataSource.searchVenues(location, VENUE_SEARCH_RESULT_LIMIT)
        if (dataSource.isRemoteDataSource()) {
            handleRemoteSearchResult(venueSearchResult)
        } else {
            handleLocalSearchResult(venueSearchResult)
        }
    }

    fun fetchVenueDetails(venue: Venue) {
        executor.submit { fetchVenueDetails(getDataSource(), venue) }
    }

    private fun fetchVenueDetails(dataSource: DataSource, venue: Venue) {
        val venueDetailsResult = dataSource.fetchVenueDetails(venue.id)
        if (dataSource.isRemoteDataSource()) {
            handleRemoteVenueDetailsResult(venueDetailsResult)
        } else {
            handleLocalVenueDetailsResult(venueDetailsResult)
        }
    }

    private fun getDataSource() = if (connectivityChecker.isNetworkConnected()) remoteDataSource else localDataSource


    /**
     * Decides the next step for the [VenueSearchResult] fetched from network
     *
     * if success -> notify the callback
     * if network error -> check if the data can be fetched from local storage
     * any other error -> can't help just notify the callback
     *
     */
    private fun handleRemoteSearchResult(venueSearchResult: VenueSearchResult) {
        when {
            venueSearchResult.isSuccess -> {
                localDataSource.saveSearchResult(venueSearchResult.venues)
                callback.onVenueSearchResponse(venueSearchResult)
            }
            venueSearchResult.networkError -> searchVenues(localDataSource, venueSearchResult.searchLocation)
            else -> callback.onVenueSearchResponse(venueSearchResult)
        }
    }

    /**
     * Decides the next step for the [VenueSearchResult] fetched from Local storage
     *
     * if success -> Local storage has been accessed, keep the data in sync
     * else -> cannot find the requested data in storage, no need to sync
     */
    private fun handleLocalSearchResult(venueSearchResult: VenueSearchResult) {
        if (venueSearchResult.isSuccess) {
            syncJobScheduler.scheduleSyncJob()
        }
        callback.onVenueSearchResponse(venueSearchResult)
    }

    /**
     * Decides the next step for the [VenueDetailsResult] fetched from network
     *
     * if success -> notify the callback
     * if network error -> check if the data can be fetched from local storage
     * any other error -> can't help just notify the callback
     *
     */
    private fun handleRemoteVenueDetailsResult(result: VenueDetailsResult) {
        if (result.isSuccess) {
            localDataSource.saveVenueDetails(result.venueDetails!!)
            callback.onVenueDetailsResponse(result)
        } else if (result.networkError) {
            localDataSource.fetchVenueDetails(result.venueId)
        } else {
            callback.onVenueDetailsResponse(result)
        }
    }

    /**
     * Decides the next step for the [VenueDetailsResult] fetched from Local storage
     *
     * if success -> Local storage has been accessed, keep the data in sync
     * else -> cannot find the requested data in storage, no need to sync
     */
    private fun handleLocalVenueDetailsResult(result: VenueDetailsResult) {
        if (result.isSuccess) {
            syncJobScheduler.scheduleSyncJob()
        }
        callback.onVenueDetailsResponse(result)
    }

    private fun DataSource.isRemoteDataSource() = this is RetrofitDataSource
}