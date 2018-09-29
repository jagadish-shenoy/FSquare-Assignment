package com.abnamroassignment.foreaquare

import android.content.Context
import com.abnamroassignment.foreaquare.datasource.local.DatabaseDataSource
import com.abnamroassignment.foreaquare.datasource.remote.RetrofitDataSource

abstract class DataSource(protected val context: Context) {

    var callback: DataSource.Callback? = null

    abstract fun searchVenues(location:String, limit:Int)

    abstract fun fetchVenueDetails(venueId:String)


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
                                            private val localDataSource: StorageDataSource,
                                            private val remoteDataSource: DataSource) : DataSource.Callback {

    init {
        localDataSource.callback = this
        remoteDataSource.callback = this
    }

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

    interface Callback {

        fun onVenueSearchResponse(venueSearchResult: VenueSearchResult)

        fun onVenueDetailsResponse(venueDetailsResult: VenueDetailsResult)
    }

    constructor(context: Context, callback: ForeSquareManagerCallback) : this(callback,
            DatabaseDataSource(context),
            RetrofitDataSource(context))


    fun searchVenues(location: String) {
        remoteDataSource.searchVenues(location, VENUE_SEARCH_RESULT_LIMIT)
    }

    fun fetchVenueDetails(venue: Venue) {
        remoteDataSource.fetchVenueDetails(venue.id)
    }
}

private fun DataSource.isRemoteDataSource() = this is RetrofitDataSource