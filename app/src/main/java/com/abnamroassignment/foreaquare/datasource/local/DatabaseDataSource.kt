package com.abnamroassignment.foreaquare.datasource.local

import android.content.Context
import com.abnamroassignment.foreaquare.StorageDataSource
import com.abnamroassignment.foreaquare.Venue
import com.abnamroassignment.foreaquare.VenueDetails

class DatabaseDataSource(context: Context) : StorageDataSource(context) {

    private val venueDatabase = VenueDatabase.getInMemoryDatabase(context)

    override fun saveSearchResult(venues: List<Venue>) {
        venueDatabase.venuesDao().insertVenues(*venues.toTypedArray())
    }

    override fun saveVenueDetails(venueDetails: VenueDetails) {
        venueDatabase.venueDetailsDao().insertVenueDetails(venueDetails)
    }


    override fun searchVenues(location: String, limit: Int) {
        venueDatabase.venuesDao().getVenuesForLocation(location, limit)
    }

    override fun fetchVenueDetails(venueId: String) {
        venueDatabase.venueDetailsDao().getVenueDetails(venueId)
    }
}