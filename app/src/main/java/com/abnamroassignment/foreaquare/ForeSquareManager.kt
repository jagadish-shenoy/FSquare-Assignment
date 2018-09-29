package com.abnamroassignment.foreaquare

import com.abnamroassignment.foreaquare.datasource.local.DatabaseDataSource
import com.abnamroassignment.foreaquare.datasource.remote.RetrofitDataSource

abstract class DataSource(protected val callback:DataSource.Callback) {

    abstract fun searchVenues(location:String, limit:Int)

    abstract fun fetchVenueDetails(venueId:String)


    interface Callback {

        fun onVenueSearchResponse(venueSearchResult: VenueSearchResult)

        fun onVenueDetailsResponse(venueDetailsResult: VenueDetailsResult)
    }

}

class ForeSquareManager private constructor(private val callback: DataSource.Callback,
                                            private val localDataSource: DataSource,
                                            private val remoteDataSource:DataSource) {


    constructor(callback: DataSource.Callback) :this(callback,

            DatabaseDataSource(object : DataSource.Callback {
                override fun onVenueSearchResponse(venueSearchResult: VenueSearchResult) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onVenueDetailsResponse(venueDetailsResult: VenueDetailsResult) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }),

            RetrofitDataSource(object:DataSource.Callback {
                override fun onVenueSearchResponse(venueSearchResult: VenueSearchResult) {
                    callback.onVenueSearchResponse(venueSearchResult)
                }

                override fun onVenueDetailsResponse(venueDetailsResult: VenueDetailsResult) {
                    callback.onVenueDetailsResponse(venueDetailsResult)
                }

            }))


    fun searchVenues(location: String) {
        remoteDataSource.searchVenues(location, VENUE_SEARCH_RESULT_LIMIT)
    }

    fun fetchVenueDetails(venue: Venue) {
        remoteDataSource.fetchVenueDetails(venue.id)
    }
}