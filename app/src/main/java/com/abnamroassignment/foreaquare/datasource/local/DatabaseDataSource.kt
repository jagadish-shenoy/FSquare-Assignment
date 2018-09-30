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

    override fun searchVenues(location: String, limit: Int) {
        val venues = venueDatabase.venuesDao().getVenuesForLocation("%$location%", limit)
        callback?.onVenueSearchResponse(this, VenueSearchResult(Status.SUCCESS, venues))
    }

    override fun fetchVenueDetails(venueId: String) {
        val venueDetailsResult = fetchVenueDetailsSync(venueId)
        callback?.onVenueDetailsResponse(this, venueDetailsResult)
    }

    override fun fetchVenueDetailsSync(venueId: String): VenueDetailsResult {
        val venueDetails = venueDatabase.venueDetailsDao().getVenueDetails(venueId)
        val status = if (venueDetails == null) Status.NETWORK_ERROR else Status.SUCCESS
        return VenueDetailsResult(status, venueDetails)
    }

    override fun getAllVenueIds() = venueDatabase.venuesDao().getAllVenueIds()
}