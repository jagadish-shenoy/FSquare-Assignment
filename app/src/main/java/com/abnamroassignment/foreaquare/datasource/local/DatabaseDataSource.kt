package com.abnamroassignment.foreaquare.datasource.local

import android.content.Context
import com.abnamroassignment.foreaquare.StorageDataSource
import com.abnamroassignment.foreaquare.Venue
import com.abnamroassignment.foreaquare.VenueDetails

class DatabaseDataSource(context: Context) : StorageDataSource(context) {

    override fun saveSearchResult(venues: List<Venue>) {

    }

    override fun saveVenueDetails(venueDetails: VenueDetails) {

    }


    override fun searchVenues(location: String, limit: Int) {

    }

    override fun fetchVenueDetails(venueId: String) {

    }
}