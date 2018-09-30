package com.abnamroassignment.foreaquare

import android.content.Context
import com.abnamroassignment.foreaquare.datasource.local.DatabaseDataSource
import com.abnamroassignment.foreaquare.datasource.remote.RetrofitDataSource
import com.abnamroassignment.networking.ConnectivityChecker
import com.abnamroassignment.networking.ConnectivityCheckerImpl

abstract class DataSource(protected val context: Context) {

    var callback: DataSource.Callback? = null

    abstract fun searchVenues(location: String, limit: Int)

    abstract fun fetchVenueDetails(venueId: String)


    interface Callback {

        fun onVenueSearchResponse(dataSource: DataSource, venueSearchResult: VenueSearchResult)

        fun onVenueDetailsResponse(dataSource: DataSource, venueDetailsResult: VenueDetailsResult)
    }

}

abstract class StorageDataSource(context: Context) : DataSource(context) {

    abstract fun saveSearchResult(venues: List<Venue>)

    abstract fun saveVenueDetails(venueDetails: VenueDetails)
}

class ForeSquareManager private constructor(private val callback: ForeSquareManagerCallback,
                                            private val connectivityChecker: ConnectivityChecker,
                                            private val localDataSource: StorageDataSource,
                                            private val remoteDataSource: DataSource) : DataSource.Callback {

    init {
        localDataSource.callback = this
        remoteDataSource.callback = this
    }

    constructor(context: Context, callback: ForeSquareManagerCallback) : this(callback,
            ConnectivityCheckerImpl(context),
            DatabaseDataSource(context),
            RetrofitDataSource(context))

    interface ForeSquareManagerCallback {

        fun onVenueSearchResponse(venueSearchResult: VenueSearchResult)

        fun onVenueDetailsResponse(venueDetailsResult: VenueDetailsResult)
    }

    override fun onVenueSearchResponse(dataSource: DataSource, venueSearchResult: VenueSearchResult) {
        if (dataSource.isRemoteDataSource()) {
            if (!venueSearchResult.venues.isEmpty()) {
                localDataSource.saveSearchResult(venueSearchResult.venues)
            }
        }
        callback.onVenueSearchResponse(venueSearchResult)
    }

    override fun onVenueDetailsResponse(dataSource: DataSource, venueDetailsResult: VenueDetailsResult) {
        if (dataSource.isRemoteDataSource()) {
            venueDetailsResult.venueDetails?.apply { localDataSource.saveVenueDetails(this) }
        }
        callback.onVenueDetailsResponse(venueDetailsResult)
    }

    fun searchVenues(location: String) {
        getDataSource().searchVenues(location, VENUE_SEARCH_RESULT_LIMIT)
    }

    fun fetchVenueDetails(venue: Venue) {
        getDataSource().fetchVenueDetails(venue.id)
    }

    private fun getDataSource() = if (connectivityChecker.isNetworkConnected()) remoteDataSource else localDataSource
}

private fun DataSource.isRemoteDataSource() = this is RetrofitDataSource