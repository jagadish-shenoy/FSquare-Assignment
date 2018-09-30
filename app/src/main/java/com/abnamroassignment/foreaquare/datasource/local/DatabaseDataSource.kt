package com.abnamroassignment.foreaquare.datasource.local

import android.content.Context
import com.abnamroassignment.foreaquare.*

class DatabaseDataSource(context: Context) : StorageDataSource(context) {

    private val venueDatabase = VenueDatabase.getInMemoryDatabase(context)

    override fun saveSearchResult(venues: List<Venue>) {
        venueDatabase.venuesDao().insertVenues(*venues.toTypedArray())
    }

    override fun saveVenueDetails(venueDetails: VenueDetails) {
        venueDatabase.venueDetailsDao().insertVenueDetails(venueDetails)
    }

    override fun searchVenues(location: String, limit: Int): VenueSearchResult {
        val venues = venueDatabase.venuesDao().getVenuesForLocation("%$location%", limit)
        return if (venues.isEmpty()) {
            VenueSearchResult(location, Status.NETWORK_ERROR, venues)
        } else {
            VenueSearchResult(location, Status.SUCCESS, venues)
        }
    }

    override fun fetchVenueDetails(venueId: String): VenueDetailsResult {
        val venueDetails = venueDatabase.venueDetailsDao().getVenueDetails(venueId)
        val status = if (venueDetails == null) Status.CACHE_MISS else Status.SUCCESS
        return VenueDetailsResult(venueId, venueDetails, status)
    }

    override fun getAllVenueIds() = venueDatabase.venuesDao().getAllVenueIds()
}